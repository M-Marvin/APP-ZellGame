package de.game.gamelogic.zellpropertys;

import java.util.List;

import de.game.util.Direction;

public class ZPropertyDirection extends ZProperty<Direction> {

	public ZPropertyDirection(String name) {
		super(name);
	}

	@Override
	public List<Direction> getValues() {
		return Direction.valuesList();
	}

	@Override
	public Direction getDefault() {
		return Direction.RIGHT;
	}

	@Override
	public Direction valueFromString(String s) {
		return Direction.fromString(s);
	}
	
}
