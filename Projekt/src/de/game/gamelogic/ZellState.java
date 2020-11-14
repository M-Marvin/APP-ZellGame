package de.game.gamelogic;

import java.util.HashMap;
import java.util.Map.Entry;

import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.util.RotDirection;
import de.game.util.Vec2i;

public class ZellState {

	public static final ZellState EMPTY = new ZellState();
	
	protected Zell zellType;
	@SuppressWarnings("rawtypes")
	protected HashMap<ZProperty, Object> propertys;
	
	protected int curentAnimationRot = 0;
	protected Vec2i curentAnimationPos = new Vec2i(0, 0);
	
	@SuppressWarnings("rawtypes")
	public ZellState(Zell zellType, ZProperty[] propertys) {
		this.zellType = zellType;
		this.propertys = new HashMap<ZProperty, Object>();
		for (ZProperty prop : propertys) {
			this.propertys.put(prop, prop.getDefault());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private ZellState() {
		this.zellType = Zell.EMPTY;
		this.propertys = new HashMap<ZProperty, Object>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ZellState copy() {
		ZellState state = new ZellState();
		state.zellType = this.zellType;
		state.propertys = (HashMap<ZProperty, Object>) this.propertys.clone();
		return state;
	}
	
	public Zell getZell() {
		return zellType;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ZProperty[] getPropertys() {
		return ((HashMap<ZProperty, Object>) this.propertys.clone()).keySet().toArray(new ZProperty[] {});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getProperty(ZProperty<T> property) {
		
		for (Entry<ZProperty, Object> entry : propertys.entrySet()) {
			
			if (entry.getKey().equals(property)) {
				
				T value = (T) entry.getValue();
				return value;
				
			}
			
		}
		
		return property.getDefault();
		
	}
	
	@SuppressWarnings("rawtypes")
	public <T> void setProperty(ZProperty<T>  property, T value) {
		
		for (Entry<ZProperty, Object> entry : propertys.entrySet()) {
			
			if (entry.getKey().equals(property)) {
				
				entry.setValue(value);
				
			}
			
		}
		
	}
	
	public void rotate(RotDirection direction) {
		
		if (this.zellType.canBeRotated(this)) {
			
			this.zellType.onRotate(direction, this);
			this.startAnimation(direction);
			
		}
		
	}
	
	public boolean isEmpty() {
		return this.getZell().equals(Zell.EMPTY);
	}
	
	public void startAnimation(RotDirection direction) {
		
		float angle = direction == RotDirection.CW ? -90 : 90;
		this.curentAnimationRot += angle;
		
	}
	
	public void startAnimation(Vec2i from, Vec2i to) {
		
		this.curentAnimationPos = new Vec2i(from.x * 20 - to.x * 20, from.y * 20 - to.y * 20);
		
	}
	
	public boolean tickAnimation() {
		
		int stepSize = 2;
		
		if (curentAnimationPos.x != 0) {
			curentAnimationPos.x += curentAnimationPos.x < 0 ? stepSize : -stepSize;
		} else if (curentAnimationPos.y != 0) {
			curentAnimationPos.y += curentAnimationPos.y < 0 ? stepSize : -stepSize;
		}
		
		int stepSizeRot = 15;
		
		if (curentAnimationRot != 0) {
			
			curentAnimationRot += curentAnimationRot < 0 ? stepSizeRot : -stepSizeRot;
			
		}
		
		boolean flag = 0 == curentAnimationPos.x && 0 == curentAnimationPos.y && curentAnimationRot == 0;
		
		return flag;
		
	}
	
	public boolean isAnimationStarting() {
		return 	curentAnimationPos.x < 0 ? curentAnimationPos.x < -10 : curentAnimationPos.x > 10 ||
				curentAnimationPos.y < 0 ? curentAnimationPos.y < -10 : curentAnimationPos.y > 10;
	}
	
	public boolean isRotating() {
		return curentAnimationRot != 0;
	}
	
	public Vec2i getCurentAnimationPos() {
		return curentAnimationPos;
	}
	
	public int getCurentAnimationRot() {
		return curentAnimationRot;
	}
	
	private int currentProp = 0;
	private int curentState = 0;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void iteratePropertys() {
		
		if (this.propertys.keySet().size() > 0) {
			ZProperty prop = this.propertys.keySet().toArray(new ZProperty[] {})[currentProp];
			
			setProperty(prop, prop.getValues().get(curentState));
			curentState++;
			
			if (curentState >= prop.getValues().size()) {
				currentProp++;
				if (currentProp >= this.propertys.keySet().size()) {
					currentProp = 0;
				}
				curentState = 0;
			}
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		
		if (this.zellType == Zell.EMPTY) {
			
			return "";
			
		} else {
			
			String zellName = GameRegistry.getNameForZell(zellType);
			String propertyString = "";
			for (Entry<ZProperty, Object> entry : this.propertys.entrySet()) {
				propertyString += entry.getKey().toString() + "=" + entry.getValue().toString() + ", ";
			}
			if (propertyString != "") propertyString = propertyString.substring(0, propertyString.length() - 2);
			
			return zellName + "{" + propertyString + "}";
			
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ZellState fromString(String s) {
		
		if (s.equals("")) {
			
			return ZellState.EMPTY;
			
		} else {
			
			String zellName = s.split("\\{")[0];
			Zell zellType = GameRegistry.getZellByName(zellName);
			ZellState state = zellType.getDefaultState();
			String propertyString = s.split("\\{")[1];
			propertyString = propertyString.length() > 1 ? propertyString.split("\\}")[0] : "";
			
			ZProperty[] props = state.getPropertys();
			
			for (ZProperty prop : props) {
				
				String propS = prop.toString();
				
				for (String propertyStringE : propertyString.split(",")) {
					
					if (propertyStringE.contains(propS)) {
						
						String propValueS = propertyStringE.substring(propS.length() + 1);
						
						Object value = prop.valueFromString(propValueS);
						
						state.setProperty(prop, value);
						
					}
					
				}
				
			}
			
			return state;
			
		}
		
	}
	
}
