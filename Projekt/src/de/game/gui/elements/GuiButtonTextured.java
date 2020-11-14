package de.game.gui.elements;

import java.awt.Color;

import de.game.Engine;
import de.game.gui.GuiScreen;
import de.game.render.TextureIO;

public abstract class GuiButtonTextured extends GuiButton {
	
	public static final int BUTTON_STRING_BORDER_WIDTH_LEFT_RIGHT = 20;
	public static final int BUTTON_STRING_BORDER_WIDTH_UP_DOWN = 10;
	
	public static final Color COLOR_NORMAL = new Color(20, 20, 20, 100);
	public static final Color COLOR_GREEN = new Color(0, 255, 0, 100);
	public static final Color COLOR_RED = new Color(255, 0, 0, 100);
	public static final Color COLOR_MOUSE_HOVER = new Color(255, 255, 0, 150);
	
	protected int x, y;
	protected TextureIO texture;
	protected int width;
	protected int height;
	protected boolean isMouseOver;
	protected boolean isPressed;
	protected Color color;

	public GuiButtonTextured(int posX, int posY, int width, int height, String texture, Color color) {
		super(posX, posY, 0, "", color);
		
		this.x = posX;
		this.y = posY;
		this.texture = Engine.getInstance().getResourceLoader().getTexture(texture);
		this.color = color;
		
		this.width = width;
		this.height = height;
		
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
		
		GuiScreen.drawElementQuad(x, y, width, height, button_color);
		
		GuiScreen.drawTexture(x, y, this.width, this.height, texture);
		
	}
	
	@Override
	public boolean isMouseOver(float mouseX, float mouseY) {
		
		boolean flag = (mouseX >= x && mouseX <= width + x) && (mouseY >= y && mouseY <= height + y);
			
		if (!isMouseOver && flag) {
			
			Engine.getInstance().getSoundPlayer().playSound("button_hover");
			
		}
		
		isMouseOver = flag;
		
		return isMouseOver;
		
	}

	public void setTexture(String texture) {
		this.texture = Engine.getInstance().getResourceLoader().getTexture(texture);
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
		return this.height;
	}
	
}
