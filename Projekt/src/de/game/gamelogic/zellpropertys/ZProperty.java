package de.game.gamelogic.zellpropertys;

import java.util.List;

import de.game.gamelogic.GameRegistry;

public abstract class ZProperty<T extends Object> {
	
	protected String name;
	
	@SuppressWarnings("unused")
	private ZProperty() {};
	
	public ZProperty(String name) {
		this.name = name;
	}
	
	public abstract List<T> getValues();
	
	public boolean isValueValid(T value) {
		return this.getValues().contains(value);
	}

	public abstract T getDefault();

	@Override
	public String toString() {
		return GameRegistry.getNameForProp(this.getClass()) + "[name=" + name + "]";
	}
	
	public abstract T valueFromString(String s);
	
}
