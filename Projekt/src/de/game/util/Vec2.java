package de.game.util;

public class Vec2 {
	
	public float x;
	public float y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2 mult(float m) {
		return new Vec2(x * m, y * m);
	}
	
	public Vec2 copy() {
		return new Vec2(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vec2) {
			return ((Vec2)obj).x == x && ((Vec2)obj).y == y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Vec2[" + x + ", " + y + "]";
	}
	
}
