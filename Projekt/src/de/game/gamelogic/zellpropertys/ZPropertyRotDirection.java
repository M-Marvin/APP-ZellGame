package de.game.gamelogic.zellpropertys;

import java.util.List;

import de.game.util.RotDirection;

public class ZPropertyRotDirection extends ZProperty<RotDirection> {

	public ZPropertyRotDirection(String name) {
		super(name);
	}

	@Override
	public List<RotDirection> getValues() {
		return RotDirection.valuesList();
	}

	@Override
	public RotDirection getDefault() {
		return RotDirection.CW;
	}

	@Override
	public RotDirection valueFromString(String s) {
		return RotDirection.fromString(s);
	}
	
}
