package de.game.util;

public class Vec2i {
	
	public int x;
	public int y;
	
	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2i mult(int m) {
		return new Vec2i(x * m, y * m);
	}
	
	public Vec2i copy() {
		return new Vec2i(x, y);
	}

	public Vec2i offset(Direction direction) {
		
		switch (direction) {
		case UP: return new Vec2i(x, y + 1);
		case DOWN: return new Vec2i(x, y - 1);
		case LEFT: return new Vec2i(x - 1, y);
		case RIGHT: return new Vec2i(x + 1, y);
		default: return this;
		}
		
	}
	
	public Vec2i offset(Direction direction, int index) {
		
		switch (direction) {
		case UP: return new Vec2i(x, y + index);
		case DOWN: return new Vec2i(x, y - index);
		case LEFT: return new Vec2i(x - index, y);
		case RIGHT: return new Vec2i(x + index, y);
		default: return this;
		}
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vec2i) {
			return ((Vec2i)obj).x == x && ((Vec2i)obj).y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x >> y;
	}
	
	@Override
	public String toString() {
		return "Vec2i[" + x + ", " + y + "]";
	}

	public boolean isNegativ() {
		return x < 0 || y < 0;
	}
	
}
