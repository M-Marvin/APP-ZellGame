package de.game.util;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.game.Engine;

public class InputHandler {

	public HashMap<String, Integer> keyBingings;
	public HashMap<Integer, Boolean> pressedKeys;
	
	public HashMap<Integer, Boolean> pressedMKeys;
	
	private float mouseDX;
	private float mouseDY;
	private float mouseDW;
	
	public InputHandler() {
		keyBingings = new HashMap<>();
		pressedKeys = new HashMap<>();
		pressedMKeys = new HashMap<>();
	}
	
	public boolean isTyped(String keybinding) {
		
		int key = keyBingings.get(keybinding);
		
		if (Keyboard.isKeyDown(key) && !pressed(key)) {
			setPressed(key, true);
			return true;
		} else if (!Keyboard.isKeyDown(key)) {
			setPressed(key, false);
		}
		
		return false;
		
	}
	
	public boolean isPressed(String keybinding) {
		
		int key = keyBingings.get(keybinding);
		
		return Keyboard.isKeyDown(key);
		
	}

	public void registerKeybinging(String name, int key) {
		
		keyBingings.put(name, key);
		
	}
	
	private boolean pressed(int key) {
		return pressedKeys.containsKey(key) ? pressedKeys.get(key) : false;
	}
	
	private void setPressed(int key, boolean pressed) {
		if (pressedKeys.containsKey(key)) {
			pressedKeys.replace(key, pressed);
		} else {
			pressedKeys.put(key, pressed);
		}
	}
	
	private boolean Mpressed(int key) {
		return pressedMKeys.containsKey(key) ? pressedMKeys.get(key) : false;
	}
	
	private void setMPressed(int key, boolean pressed) {
		if (pressedMKeys.containsKey(key)) {
			pressedMKeys.replace(key, pressed);
		} else {
			pressedMKeys.put(key, pressed);
		}
	}
	
	public boolean isMouseKeyTyped(int key) {
		
		if (Mouse.isButtonDown(key) && !Mpressed(key)) {
			setMPressed(key, true);
			return true;
		} else if (!Mouse.isButtonDown(key)) {
			setMPressed(key, false);
		}
		
		return false;
		
	}
	
	public float getMouseDX() {
		return mouseDX;
	}
	
	public float getMouseDY() {
		return mouseDY;
	}
	
	public float getMouseX() {
		return (float) Mouse.getX() / Display.getWidth() * Engine.viewSizeX;
	}
	
	public float getMouseY() {
		return (float) Mouse.getY() / Display.getHeight() * Engine.viewSizeY;
	}
	
	public float getMouseDW() {
		return mouseDW;
	}
	
	public void update() {
		
		this.mouseDX = Mouse.getDX();
		this.mouseDY = Mouse.getDY();
		this.mouseDW = Mouse.getDWheel();
		
	}

	public float getWorldZoom(float worldZoom) {
		return this.getMouseDW() / 1210F * worldZoom;
	}
	
	public Vec2 getWorldCoursorPos() {
		float mx = getMouseX();
		float my = getMouseY();
		float worldX = (mx - Engine.getInstance().worldTranslate.x) / Engine.getInstance().worldZoom;
		float worldY = (my - Engine.getInstance().worldTranslate.y) / Engine.getInstance().worldZoom;
		return new Vec2(worldX, worldY);
	}
	
}
