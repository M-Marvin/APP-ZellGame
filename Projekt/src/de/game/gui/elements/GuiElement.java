package de.game.gui.elements;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector4f;

public abstract class GuiElement {
	
	protected boolean isEnabled = true;
	
	public abstract void draw();
	
	public abstract void onPressed(float mouseX, float mouseY);
	
	public abstract void onReleased(float mouseX, float mouseY);
	
	public abstract boolean isMouseOver(float mouseX, float mouseY);
	
	public Vector4f calculatePosition(Vector4f cord) {
		
		int size = Math.min(Display.getWidth(), Display.getHeight());
		int l = (Display.getWidth() - size) / 2;
		int j = (Display.getHeight() - size) / 2;
		
		float x = cord.x / 256F;
		float y = cord.y / 256F;
		float width = cord.z / 256F;
		float height = cord.w / 256F;
		
		return new Vector4f(x * size + l, y * size + j, width * size, height * size);
		
	}
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}
