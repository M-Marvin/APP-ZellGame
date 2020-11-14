package de.game.gui.elements;

import java.awt.Color;

import de.game.Engine;
import de.game.gui.GuiScreen;

public abstract class GuiButton extends GuiElement {
	
	public static final int BUTTON_STRING_BORDER_WIDTH_LEFT_RIGHT = 20;
	public static final int BUTTON_STRING_BORDER_WIDTH_UP_DOWN = 10;
	
	public static final Color COLOR_NORMAL = new Color(20, 20, 20, 100);
	public static final Color COLOR_GREEN = new Color(0, 255, 0, 100);
	public static final Color COLOR_RED = new Color(255, 0, 0, 100);
	public static final Color COLOR_MOUSE_HOVER = new Color(255, 255, 0, 150);
	
	protected int x, y;
	protected String displayString;
	protected int width;
	protected boolean isMouseOver;
	protected boolean isPressed;
	protected Color color;
	
	public GuiButton(int posX, int posY, String displayString, Color color) {
		
		this.x = posX;
		this.y = posY;
		this.displayString = displayString;
		int w = displayString.toCharArray().length * GuiScreen.DEFAULT_STRING_SIZE;
		this.width = BUTTON_STRING_BORDER_WIDTH_LEFT_RIGHT * 2 + w;
		this.color = color;
		
	}
	
	public GuiButton(int posX, int posY, int width, String displayString, Color color) {
		
		this.x = posX;
		this.y = posY;
		this.displayString = displayString;
		this.width = width;
		this.color = color;
		
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void draw() {
		
		Color button_color = color;
		
		if (isMouseOver) {
			button_color = COLOR_MOUSE_HOVER;
		}
		
		float offset = width / 2 - (displayString.toCharArray().length * GuiScreen.DEFAULT_STRING_SIZE) / 2;
		
		GuiScreen.drawElementQuad(x, y, width, BUTTON_STRING_BORDER_WIDTH_UP_DOWN * 2 + GuiScreen.DEFAULT_STRING_SIZE, button_color);
		
		GuiScreen.drawString(x + offset, y + BUTTON_STRING_BORDER_WIDTH_UP_DOWN, displayString);
		
	}
	
	@Override
	public boolean isMouseOver(float mouseX, float mouseY) {
		
		int widthT = BUTTON_STRING_BORDER_WIDTH_LEFT_RIGHT * 2 + this.width;
		int height = BUTTON_STRING_BORDER_WIDTH_UP_DOWN * 2 + GuiScreen.DEFAULT_STRING_SIZE;
		
		boolean flag = (mouseX >= x && mouseX <= widthT + x) && (mouseY >= y && mouseY <= height + y);
			
		if (!isMouseOver && flag) {
			
			Engine.getInstance().getSoundPlayer().playSound("button_hover");
			
		}
		
		isMouseOver = flag;
		
		return isMouseOver;
		
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	
	@Override
	public void onPressed(float mouseX, float mouseY) {
		if (!isPressed) {
			isPressed = true;
			Engine.getInstance().getSoundPlayer().playSound("button_click");
			this.onClicked(mouseX, mouseY);
		}
	}
	
	@Override
	public void onReleased(float mouseX, float mouseY) {
		isPressed = false;
	}
	
	public abstract void onClicked(float mouseX, float mouseZ);
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return BUTTON_STRING_BORDER_WIDTH_UP_DOWN * 2 + GuiScreen.DEFAULT_STRING_SIZE;
	}
	
}
