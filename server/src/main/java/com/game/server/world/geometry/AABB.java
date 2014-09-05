package com.game.server.world.geometry;

/**
 * @author dohnal
 */

/**
 * Represents an axis aligned bounding box.
 */
public class AABB
{
	/** The minimum extent */
	private Vector2 min;

	/** The maximum extent */
	private Vector2 max;

	public AABB(double minX, double minY, double maxX, double maxY)
	{
		this(new Vector2(minX, minY), new Vector2(maxX, maxY));
	}

	public AABB(Vector2 min, Vector2 max)
	{
		// check the min and max
		if (min.getX() > max.getX() || min.getY() > max.getY())
		{
			throw new IllegalArgumentException("Invalid min,max extents");
		}

		this.min = min;
		this.max = max;
	}

	/**
	 * @param radius the radius of a circle fitting inside an AABB
	 */
	public AABB(double radius)
	{
		this(null, radius);
	}

	/**
	 * Creates an AABB for a circle with the given center and radius.
	 * @param center the center of the circle
	 * @param radius the radius of the circle
	 */
	public AABB(Vector2 center, double radius)
	{
		if (radius < 0) throw new IllegalArgumentException("Invalid radius");

		if (center == null)
		{
			this.min = new Vector2(-radius, -radius);
			this.max = new Vector2( radius,  radius);
		}
		else
		{
			this.min = new Vector2(center.getX() - radius, center.getY() - radius);
			this.max = new Vector2(center.getX() + radius, center.getY() + radius);
		}
	}

	public AABB(AABB aabb)
	{
		this.min = aabb.getMin().copy();
		this.max = aabb.getMax().copy();
	}

	/**
	 * Translates the AABB by the given translation.
	 */
	public void translate(Vector2 translation)
	{
		this.getMax().add(translation);
		this.getMin().add(translation);
	}

	/**
	 * Returns the width of this AABB.
	 */
	public double getWidth()
	{
		return this.max.getX() - this.min.getX();
	}

	/**
	 * Returns the height of this AABB.
	 */
	public double getHeight()
	{
		return this.max.getY() - this.min.getY();
	}

	/**
	 * Returns the perimeter of this AABB.
	 */
	public double getPerimeter()
	{
		return 2 * (this.max.getX() - this.min.getX() + this.max.getY() - this.min.getY());
	}

	/**
	 * Returns the area of this AABB.
	 */
	public double getArea()
	{
		return (this.max.getX() - this.min.getX()) * (this.max.getY() - this.min.getY());
	}

	/**
	 * Performs a union of this AABB and the given AABB placing
	 * the result of the union into this AABB.
	 */
	public AABB union(AABB aabb)
	{
		this.min.setX(Math.min(this.min.getX(), aabb.min.getX()));
		this.min.setY(Math.min(this.min.getY(), aabb.min.getY()));
		this.max.setX(Math.max(this.max.getX(), aabb.max.getX()));
		this.max.setY(Math.max(this.max.getY(), aabb.max.getY()));

		return this;
	}

	/**
	 * Performs a union of this AABB and the given AABB returning
	 * a new AABB containing the result.
	 */
	public AABB getUnion(AABB aabb)
	{
		return new AABB(
				new Vector2(Math.min(this.min.getX(), aabb.min.getX()), Math.min(this.min.getY(), aabb.min.getY())),
				new Vector2(Math.max(this.max.getX(), aabb.max.getX()), Math.max(this.max.getY(), aabb.max.getY())));
	}

	/**
	 * Performs the intersection of this AABB and the given AABB placing
	 * the result into this AABB.
	 *
	 * If the given AABB does not overlap this AABB, this AABB is
	 * set to a zero AABB.
	 */
	public void intersection(AABB aabb)
	{
		this.min.setX(Math.max(this.min.getX(), aabb.min.getX()));
		this.min.setY(Math.max(this.min.getY(), aabb.min.getY()));
		this.max.setX(Math.min(this.max.getX(), aabb.max.getX()));
		this.max.setY(Math.min(this.max.getY(), aabb.max.getY()));

		// check for a bad AABB
		if (this.min.getX() > this.max.getX() || this.min.getY() > this.max.getY())
		{
			// the two AABBs were not overlapping
			// set this AABB to a zero one
			this.min.setX(0.0);
			this.min.setY(0.0);
			this.max.setX(0.0);
			this.max.setY(0.0);
		}
	}

	/**
	 * Performs the intersection of this AABB and the given AABB returning
	 * the result in a new AABB.
	 *
	 * If the given AABB does not overlap this AABB, a zero AABB is
	 * returned.
	 */
	public AABB getIntersection(AABB aabb)
	{
		Vector2 min = new Vector2();
		Vector2 max = new Vector2();

		min.setX(Math.max(this.min.getX(), aabb.min.getX()));
		min.setY(Math.max(this.min.getY(), aabb.min.getY()));
		max.setX(Math.min(this.max.getX(), aabb.max.getX()));
		max.setY(Math.min(this.max.getY(), aabb.max.getY()));

		// check for a bad AABB
		if (min.getX() > max.getX() || min.getY() > max.getY())
		{
			// the two AABBs were not overlapping
			// return a zero one
			return new AABB(new Vector2(), new Vector2());
		}

		return new AABB(min, max);
	}

	/**
	 * Expands this AABB by half the given expansion in each direction.
	 *
	 * The expansion can be negative to shrink the AABB.  However, if the expansion is
	 * greater than the current width/height, the AABB can become invalid.  In this
	 * case, the AABB will become a zero AABB at the mid point of the min and max for
	 * the respective coordinates.
	 */
	public AABB expand(double expansion)
	{
		double e = expansion * 0.5;

		this.min.subtract(0.5, 0.5);
		this.max.add(0.5, 0.5);

		// we only need to verify the new aabb if the expansion
		// was inwardly
		if (expansion < 0.0)
		{
			// if the aabb is invalid then set the min/max(es) to
			// the middle value of their current values
			if (this.min.getX() > this.max.getX())
			{
				double mid = (this.min.getX() + this.max.getX()) * 0.5;

				this.min.setX(mid);
				this.max.setY(mid);
			}

			if (this.min.getY() > this.max.getY())
			{
				double mid = (this.min.getY() + this.max.getY()) * 0.5;

				this.min.setY(mid);
				this.max.setY(mid);
			}
		}

		return this;
	}

	/**
	 * Returns a new AABB of this AABB expanded by half the given expansion
	 * in both the x and y directions.
	 *
	 * The expansion can be negative to shrink the AABB.  However, if the expansion is
	 * greater than the current width/height, the AABB can become invalid.  In this
	 * case, the AABB will become a zero AABB at the mid point of the min and max for
	 * the respective coordinates.
	 */
	public AABB getExpanded(double expansion)
	{
		double e = expansion * 0.5;

		double minx = this.min.getX() - e;
		double miny = this.min.getY() - e;
		double maxx = this.max.getX() + e;
		double maxy = this.max.getY() + e;

		// we only need to verify the new aabb if the expansion
		// was inwardly
		if (expansion < 0.0)
		{
			// if the aabb is invalid then set the min/max(es) to
			// the middle value of their current values
			if (minx > maxx)
			{
				double mid = (minx + maxx) * 0.5;

				minx = mid;
				maxx = mid;
			}

			if (miny > maxy)
			{
				double mid = (miny + maxy) * 0.5;

				miny = mid;
				maxy = mid;
			}
		}

		return new AABB(
				new Vector2(minx, miny),
				new Vector2(maxx, maxy));
	}

	/**
	 * Returns true if the given AABB and this AABB overlap.
	 */
	public boolean overlaps(AABB aabb)
	{
		// check for overlap along the x-axis
		if (this.min.getX() > aabb.max.getX() || this.max.getX() < aabb.min.getX())
		{
			// the aabbs do not overlap along the x-axis
			return false;
		} else
		{
			// check for overlap along the y-axis
			if (this.min.getY() > aabb.max.getY() || this.max.getY() < aabb.min.getY())
			{
				// the aabbs do not overlap along the y-axis
				return false;
			}
			else
			{
				return true;
			}
		}
	}

	/**
	 * Returns true if the given AABB is contained within this AABB.
	 */
	public boolean contains(AABB aabb)
	{
		if (this.min.getX() <= aabb.min.getX() && this.max.getX() >= aabb.max.getX())
		{
			if (this.min.getY() <= aabb.min.getY() && this.max.getY() >= aabb.max.getY())
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the given point is contained within this AABB.
	 */
	public boolean contains(Vector2 point)
	{
		return this.contains(point.getX(), point.getY());
	}

	/**
	 * Returns true if the given point's coordinates are contained within this AABB.
	 */
	public boolean contains(double x, double y)
	{
		if (this.min.getX() <= x && this.max.getX() >= x)
		{
			if (this.min.getY() <= y && this.max.getY() >= y)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this AABB is zero.
	 *
	 * A zero AABB is one where its min and max x or y
	 * coordinates are equal.
	 */
	public boolean isZero()
	{
		return this.min.getX() == this.max.getX() || this.min.getY() == this.max.getY();
	}

	public Vector2 getMin()
	{
		return min;
	}

	public void setMin(Vector2 min)
	{
		this.min = min;
	}

	public Vector2 getMax()
	{
		return max;
	}

	public void setMax(Vector2 max)
	{
		this.max = max;
	}

	public Vector2 getCenter()
	{
		return new Vector2((min.getX() + max.getX()) * 0.5, (min.getY() + max.getY()) * 0.5);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("AABB[Min=").append(this.min)
		  .append("|Max=").append(this.max)
		  .append("]");

		return sb.toString();
	}
}
