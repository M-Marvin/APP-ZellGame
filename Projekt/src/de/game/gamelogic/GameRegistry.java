package de.game.gamelogic;

import java.util.HashMap;
import java.util.Map.Entry;

import de.game.Engine;
import de.game.gamelogic.zellpropertys.ZProperty;

public class GameRegistry {
	
	protected HashMap<String, Zell> zellRegistry;
	@SuppressWarnings("rawtypes")
	protected HashMap<String, Class<? extends ZProperty>> propRegistry;
	
	@SuppressWarnings("rawtypes")
	public GameRegistry() {
		this.zellRegistry = new HashMap<String, Zell>();
		this.propRegistry = new HashMap<String, Class<? extends ZProperty>>();
	}
	
	public void registerZell0(Zell item, String name) {
		if (this.zellRegistry.containsKey(name)) {
			System.err.println("Duplicated Entry for Key " + name);
		} else if (this.zellRegistry.containsValue(item)) {
			System.err.println("Duplicated Registration for Item " + item);
		} else {
			this.zellRegistry.put(name, item);
		}
	}
	public static void registerZell(Zell item, String name) {
		Engine.getInstance().getGameRegistry().registerZell0(item, name);
	}
	
	public String getNameForZell0(Zell item) {
		if (this.zellRegistry.containsValue(item)) {
			for (Entry<String, Zell> entry : this.zellRegistry.entrySet()) {
				if (entry.getValue() == item) return entry.getKey();
			}
		}
		System.err.println("Item " + item + " is not registred!");
		return null;
	}
	public static String getNameForZell(Zell item) {
		return Engine.getInstance().getGameRegistry().getNameForZell0(item);
	}
	
	public Zell[] getRegistredZells0() {
		return this.zellRegistry.values().toArray(new Zell[] {});
	}
	public static Zell[] getRegistredZells() {
		return Engine.getInstance().getGameRegistry().getRegistredZells0();
	}
	
	public Zell getZellByName0(String name) {
		return this.zellRegistry.get(name);
	}
	public static Zell getZellByName(String name) {
		return Engine.getInstance().getGameRegistry().getZellByName0(name);
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public void registerProp0(Class<? extends ZProperty> item, String name) {
		if (this.propRegistry.containsKey(name)) {
			System.err.println("Duplicated Entry for Key " + name);
		} else if (this.propRegistry.containsValue(item)) {
			System.err.println("Duplicated Registration for Item " + item);
		} else {
			this.propRegistry.put(name, item);
		}
	}
	@SuppressWarnings("rawtypes")
	public static void registerProp(Class<? extends ZProperty> item, String name) {
		Engine.getInstance().getGameRegistry().registerProp0(item, name);
	}
	
	@SuppressWarnings("rawtypes")
	public String getNameForProp0(Class<? extends ZProperty> item) {
		if (this.propRegistry.containsValue(item)) {
			for (Entry<String, Class<? extends ZProperty>> entry : this.propRegistry.entrySet()) {
				if (entry.getValue() == item) return entry.getKey();
			}
		}
		System.err.println("Property " + item + " is not registred!");
		return null;
	}
	@SuppressWarnings("rawtypes")
	public static String getNameForProp(Class<? extends ZProperty> item) {
		return Engine.getInstance().getGameRegistry().getNameForProp0(item);
	}
	
	public Object[] getRegistredProps0() {
		return this.propRegistry.values().toArray(new Object[] {});
	}
	public static Object[] getRegistredProps() {
		return Engine.getInstance().getGameRegistry().getRegistredProps0();
	}
	
	@SuppressWarnings("rawtypes")
	public Class<? extends ZProperty> getPropByName0(String name) {
		return this.propRegistry.get(name);
	}
	@SuppressWarnings("rawtypes")
	public static Class<? extends ZProperty> getPropByName(String name) {
		return Engine.getInstance().getGameRegistry().getPropByName0(name);
	}
	
}
