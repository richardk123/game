package com.game.server.world.map;

/**
 * @author dohnal
 */

import com.game.server.world.geometry.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of an axis-aligned bounding box tree.

 * This class uses a self-balancing binary tree to store the AABBs.  The AABBs are sorted using the perimeter.
 * The perimeter heuristic is better than area for 2D because axis aligned segments have zero area.
 */
public class DynamicAABBTree<T extends Collidable> implements WorldMap<T>
{
	protected final double expansion = 0.2;

	/**
	 * Represents a node in the tree.
	 */
	protected class Node
	{
		/** The left child */
		public Node left;

		/** The right child */
		public Node right;

		/** The parent node */
		public Node parent;

		/** The height of this subtree */
		public int height;

		/** The collidable; null if this node is not a leaf node */
		public T object;

		/** The aabb containing all children */
		public AABB aabb;

		/**
		 * Returns true if this node is a leaf node.
		 */
		public boolean isLeaf()
		{
			return left == null;
		}
	}

	/** The root node of the tree */
	protected Node root;

	/** The unsorted list of proxies */
	protected List<Node> proxyList;

	/** Id to node map for fast lookup */
	protected Map<UUID, Node> proxyMap;

	public DynamicAABBTree()
	{
		this(64);
	}

	/**
	 * Allows fine tuning of the initial capacity of local storage for faster running times.
	 * @param initialCapacity the initial capacity of local storage
	 */
	public DynamicAABBTree(int initialCapacity)
	{
		this.proxyList = new ArrayList<Node>(initialCapacity);
		// 0.75 = 3/4, we can garuantee that the hashmap will not need to be rehashed
		// if we take capacity / load factor
		// the default load factor is 0.75 according to the javadocs, but lets assign it to be sure
		this.proxyMap = new HashMap<UUID, Node>(initialCapacity * 4 / 3 + 1, 0.75f);
	}

	@Override
	public void add(T object)
	{
		// get an aabb for the object
		AABB aabb = object.getAABB();

		// expand the aabb
		aabb.expand(expansion);

		// create a new node for the object
		Node node = new Node();
		node.object = object;
		node.aabb = aabb;

		// add the proxy to the list
		this.proxyList.add(node);

		// add the proxy to the map
		this.proxyMap.put(object.getId(), node);

		// insert the node into the tree
		this.insert(node);
	}

	@Override
	public void remove(T object)
	{
		// find the node in the map
		Node node = this.proxyMap.get(object.getId());

		if (node != null)
		{
			// remove the node from the tree
			this.remove(node);

			// remove the node from the list
			this.proxyList.remove(node);

			// remove the node from the map
			this.proxyMap.remove(object.getId());
		}
	}

	@Override
	public void update(T object)
	{
		// get the node from the map
		Node node = this.proxyMap.get(object.getId());

		if (node != null)
		{
			// get the new aabb
			AABB aabb = object.getAABB();

			// see if the old aabb contains the new one
			if (node.aabb.contains(aabb))
			{
				// if so, don't do anything
				return;
			}

			// otherwise expand the new aabb
			aabb.expand(this.expansion);

			// remove the current node from the tree
			this.remove(node);

			// set the new aabb
			node.aabb = aabb;

			// reinsert the node
			this.insert(node);
		}
	}

	@Override
	public void clear()
	{
		this.proxyList.clear();
		this.proxyMap.clear();
		this.root = null;
	}

	@Override
	public List<T> find(AABB aabb)
	{
		return this.findNonRecursive(aabb, root);
	}

	/**
	 * Internal method to insert a node into the tree.
	 */
	protected void insert(Node item)
	{
		if (this.root == null)
		{
			this.root = item;

			return;
		}

		// get the new node's aabb
		AABB itemAABB = item.aabb;

		// start looking for the insertion point at the root
		Node node = this.root;

		// loop until node is a leaf or we find a better location
		while (!node.isLeaf())
		{
			// get the current node's aabb
			AABB aabb = node.aabb;

			// the perimeter heuristic is better than area for 2D because
			// a line segment aligned with the x or y axis will generate
			// zero area

			// get its perimeter
			double perimeter = aabb.getPerimeter();

			// union the new node's aabb and the current aabb
			AABB union = aabb.getUnion(itemAABB);

			// get the union's perimeter
			double unionPerimeter = union.getPerimeter();

			// compute the cost of creating a new parent for the new
			// node and the current node
			double cost = 2 * unionPerimeter;

			// compute the minimum cost of descending further down the tree
			double descendCost = 2 * (unionPerimeter - perimeter);

			// get the left and right nodes
			Node left = node.left;
			Node right = node.right;

			// compute the cost of descending to the left
			double costl;

			if (left.isLeaf())
			{
				AABB u = left.aabb.getUnion(itemAABB);
				costl = u.getPerimeter() + descendCost;
			}
			else
			{
				AABB u = left.aabb.getUnion(itemAABB);
				double oldPerimeter = left.aabb.getPerimeter();
				double newPerimeter = u.getPerimeter();
				costl = newPerimeter - oldPerimeter + descendCost;
			}

			// compute the cost of descending to the right
			double costr;

			if (right.isLeaf())
			{
				AABB u = right.aabb.getUnion(itemAABB);
				costr = u.getPerimeter() + descendCost;
			}
			else
			{
				AABB u = right.aabb.getUnion(itemAABB);
				double oldPerimeter = right.aabb.getPerimeter();
				double newPerimeter = u.getPerimeter();
				costr = newPerimeter - oldPerimeter + descendCost;
			}

			// see if the cost to create a new parent node for the new
			// node and the current node is better than the children of
			// this node
			if (cost < costl && cost < costr)
			{
				break;
			}

			// if not then choose the next best node to try
			if (costl < costr)
			{
				node = left;
			}
			else
			{
				node = right;
			}
		}

		// now that we have found a suitable place, insert a new root
		// node for node and item
		Node parent = node.parent;
		Node newParent = new Node();
		newParent.parent = node.parent;
		newParent.aabb = node.aabb.getUnion(itemAABB);
		newParent.height = node.height + 1;

		if (parent != null)
		{
			// node is not the root node
			if (parent.left == node)
			{
				parent.left = newParent;
			}
			else
			{
				parent.right = newParent;
			}

			newParent.left = node;
			newParent.right = item;
			node.parent = newParent;
			item.parent = newParent;
		}
		else
		{
			// node is the root item
			newParent.left = node;
			newParent.right = item;
			node.parent = newParent;
			item.parent = newParent;
			this.root = newParent;
		}

		// fix the heights and aabbs
		node = item.parent;

		while (node != null)
		{
			// balance the current tree
			node = balance(node);

			Node left = node.left;
			Node right = node.right;

			// neither node should be null
			node.height = 1 + Math.max(left.height, right.height);
			node.aabb = left.aabb.getUnion(right.aabb);

			node = node.parent;
		}
	}

	/**
	 * Internal method to remove a node from the tree.
	 */
	protected void remove(Node node)
	{
		// check for an empty tree
		if (this.root == null) return;

		// check the root node
		if (node == this.root)
		{
			// set the root to null
			this.root = null;

			// return from the remove method
			return;
		}

		// get the node's parent, grandparent, and sibling
		Node parent = node.parent;
		Node grandparent = parent.parent;
		Node other;

		if (parent.left == node)
		{
			other = parent.right;
		}
		else
		{
			other = parent.left;
		}

		// check if the grandparent is null
		// indicating that the parent is the root
		if (grandparent != null)
		{
			// remove the node by overwriting the parent node
			// reference in the grandparent with the sibling
			if (grandparent.left == parent)
			{
				grandparent.left = other;
			}
			else
			{
				grandparent.right = other;
			}
			// set the siblings parent to the grandparent
			other.parent = grandparent;

			// finally rebalance the tree
			Node n = grandparent;

			while (n != null)
			{
				// balance the current subtree
				n = balance(n);

				Node left = n.left;
				Node right = n.right;

				// neither node should be null
				n.height = 1 + Math.max(left.height, right.height);
				n.aabb = left.aabb.getUnion(right.aabb);

				n = n.parent;
			}
		}
		else
		{
			// the parent is the root so set the root to the sibling
			this.root = other;

			// set the siblings parent to null
			other.parent = null;
		}
	}

	/**
	 * Balances the subtree using node as the root.
	 */
	protected Node balance(Node node)
	{
		Node a = node;

		// see if the node is a leaf node or if
		// it doesn't have enough children to be unbalanced
		if (a.isLeaf() || a.height < 2)
		{
			// return since there isn't any work to perform
			return a;
		}

		// get the nodes left and right children
		Node b = a.left;
		Node c = a.right;

		// compute the balance factor for node a
		int balance = c.height - b.height;

		// if the balance is off on the right side
		if (balance > 1)
		{
			// get the c's left and right nodes
			Node f = c.left;
			Node g = c.right;

			// switch a and c
			c.left = a;
			c.parent = a.parent;
			a.parent = c;

			// update c's parent to point to c instead of a
			if (c.parent != null)
			{
				if (c.parent.left == a)
				{
					c.parent.left = c;
				}
				else
				{
					c.parent.right = c;
				}
			}
			else
			{
				this.root = c;
			}

			// compare the balance of the children of c
			if (f.height > g.height)
			{
				// rotate left
				c.right = f;
				a.right = g;
				g.parent = a;

					// update the aabb
				a.aabb = b.aabb.getUnion(g.aabb);
				c.aabb = a.aabb.getUnion(f.aabb);

				// update the heights
				a.height = 1 + Math.max(b.height, g.height);
				c.height = 1 + Math.max(a.height, f.height);
			}
			else
			{
				// rotate right
				c.right = g;
				a.right = f;
				f.parent = a;

				// update the aabb
				a.aabb = b.aabb.getUnion(f.aabb);
				c.aabb = a.aabb.getUnion(g.aabb);

				// update the heights
				a.height = 1 + Math.max(b.height, f.height);
				c.height = 1 + Math.max(a.height, g.height);
			}

			// c is the new root node of the subtree
			return c;
		}

		// if the balance is off on the left side
		if (balance < -1)
		{
			// get b's children
			Node d = b.left;
			Node e = b.right;

			// switch a and b
			b.left = a;
			b.parent = a.parent;
			a.parent = b;

			// update b's parent to point to b instead of a
			if (b.parent != null)
			{
				if (b.parent.left == a)
				{
					b.parent.left = b;
				}
				else
				{
					b.parent.right = b;
				}
			}
			else
			{
				this.root = b;
			}

			// compare the balance of the children of b
			if (d.height > e.height)
			{
				// rotate left
				b.right = d;
				a.left = e;
				e.parent = a;

				// update the aabb
				a.aabb = c.aabb.getUnion(e.aabb);
				b.aabb = a.aabb.getUnion(d.aabb);

				// update the heights
				a.height = 1 + Math.max(c.height, e.height);
				b.height = 1 + Math.max(a.height, d.height);
			}
			else
			{
				// rotate right
				b.right = e;
				a.left = d;
				d.parent = a;

				// update the aabb
				a.aabb = c.aabb.getUnion(d.aabb);
				b.aabb = a.aabb.getUnion(e.aabb);

					// update the heights
				a.height = 1 + Math.max(c.height, d.height);
				b.height = 1 + Math.max(a.height, e.height);
			}

			// b is the new root node of the subtree
			return b;
		}

		// no balancing required so return the original subtree root node
		return a;
	}

	/**
	 * Internal non-recursive AABB finding method.
	 */
	protected List<T> findNonRecursive(AABB aabb, Node node)
	{
		List<T> list = new ArrayList<T>()
				;
		// perform a iterative, stack-less, traversal of the tree
		while (node != null)
		{
			// check if the current node overlaps the desired node
			if (aabb.overlaps(node.aabb))
			{
				// if they do overlap, then check the left child node
				if (node.left != null)
				{
					// if the left is not null, then check that subtree
					node = node.left;

					continue;
				}
				else
				{
					// if both are null, then this is a leaf node
					list.add(node.object);
					// if its a leaf node then we need to go back up the
					// tree and test nodes we haven't yet
				}
			}

			// if the current node is a leaf node or doesnt overlap the
			// desired aabb, then we need to go back up the tree until we
			// find the first left node who's right node is not null
			boolean nextNodeFound = false;

			while (node.parent != null)
			{
				// check if the current node the left child of its parent
				if (node == node.parent.left)
				{
					// it is, so check if the right node is non-null
					if (node.parent.right != null)
					{
						// it isn't so the sibling node is the next node
						node = node.parent.right;
						nextNodeFound = true;

						break;
					}
				}

				// if the current node isn't a left node or it is but its
				// sibling is null, go to the parent node
				node = node.parent;
			}

			// if we didn't find it then we are done
			if (!nextNodeFound) break;
		}

		return list;
	}
}
