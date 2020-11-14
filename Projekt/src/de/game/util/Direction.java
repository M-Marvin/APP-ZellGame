package de.game.util;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
	
	UP,DOWN,LEFT,RIGHT;

	public static List<Direction> valuesList() {
		List<Direction> values = new ArrayList<Direction>();
		values.add(UP);
		values.add(DOWN);
		values.add(LEFT);
		values.add(RIGHT);
		return values;
	}

	public int getRotation() {
		switch (this) {
		case UP: return 270;
		case DOWN: return 90;
		case LEFT: return 180;
		case RIGHT: return 0;
		default: return 0;
		}
	}
	
	public Direction getOpposite() {
		switch (this) {
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		default: return RIGHT;
		}
	}
	
	public Direction rotate(RotDirection d) {
		if (d == RotDirection.CW) {
			switch (this) {
			case UP: return RIGHT;
			case RIGHT: return DOWN;
			case DOWN: return LEFT;
			case LEFT: return UP;
			}
		} else {
			switch (this) {
			case UP: return LEFT;
			case LEFT: return DOWN;
			case DOWN: return RIGHT;
			case RIGHT: return UP;
			}
		}
		return RIGHT;
	}

	public static Direction fromString(String s) {
		switch (s) {
		case "UP": return UP;
		case "DOWN": return DOWN;
		case "LEFT": return LEFT;
		case "RIGHT": return RIGHT;
		default: return RIGHT;
		}
	}
	
}
