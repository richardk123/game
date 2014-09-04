package com.game.server.world.geometry;

/**
 * @author dohnal
 */

/**
 * Represents a vector in 2D space
 */
public class Vector2
{
	private double x;

	private double y;

	public Vector2()
	{
		this(0, 0);
	}

	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
	}

	public Vector2 copy()
	{
		return new Vector2(this.x, this.y);
	}

	/**
	 * Returns the distance from this point to the given point.
	 */
	public double distance(double x, double y)
	{
		return Math.hypot(this.x - x, this.y - y);
	}

	public double distance(Vector2 point)
	{
		return Math.hypot(this.x - point.getX(), this.y - point.getY());
	}

	/**
	 * Adds the given vector to this vector.
	 */
	public Vector2 add(Vector2 vector)
	{
		this.x += vector.getX();
		this.y += vector.getY();

		return this;
	}

	public Vector2 add(double x, double y)
	{
		this.x += x;
		this.y += y;

		return this;
	}

	/**
	 * Subtracts the given vector from this vector.
	 */
	public Vector2 subtract(Vector2 vector)
	{
		this.x -= vector.getX();
		this.y -= vector.getY();

		return this;
	}

	public Vector2 subtract(double x, double y)
	{
		this.x -= x;
		this.y -= y;

		return this;
	}

	/**
	 * Multiplies this vector by the given scalar.
	 */
	public Vector2 multiply(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;

		return this;
	}

	/**
	 * Returns the dot product of the given vector
	 */
	public double dot(Vector2 vector)
	{
		return this.x * vector.getX() + this.y * vector.getY();
	}

	public double dot(double x, double y)
	{
		return this.x * x + this.y * y;
	}

	/**
	 * Returns the cross product of the this vector and the given vector.
	 */
	public double cross(Vector2 vector)
	{
		return this.x * vector.getY() - this.y * vector.getX();
	}

	public double cross(double x, double y)
	{
		return this.x * y - this.y * x;
	}

	/**
	 * Returns true if the given vector is orthogonal (perpendicular)
	 * to this vector.
	 */
	public boolean isOrthogonal(Vector2 vector)
	{
		return Math.abs(this.x * vector.getX() + this.y * vector.getY()) <= Epsilon.E;
	}

	public boolean isOrthogonal(double x, double y)
	{
		return Math.abs(this.x * x + this.y * y) <= Epsilon.E;
	}

	/**
	 * Returns true if this vector is the zero.
	 */
	public boolean isZero()
	{
		return Math.abs(this.x) <= Epsilon.E && Math.abs(this.y) <= Epsilon.E;
	}

	/**
	 * Negates this vector.
	 */
	public Vector2 negate()
	{
		this.x *= -1.0;
		this.y *= -1.0;

		return this;
	}

	/**
	 * Converts this vector into a unit vector
	 */
	public Vector2 normalize()
	{
		double magnitude = Math.hypot(this.x, this.y);

		if (magnitude <= Epsilon.E) return this;

		double m = 1.0 / magnitude;

		this.x *= m;
		this.y *= m;

		return this;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj == this) return true;

		if (obj instanceof Vector2)
		{
			Vector2 vector = (Vector2)obj;
			return this.x == vector.getX() && this.y == vector.getY();
		}

		return false;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("(")
		  .append(this.x)
		  .append(", ")
		  .append(this.y)
		  .append(")");

		return sb.toString();
	}
}
