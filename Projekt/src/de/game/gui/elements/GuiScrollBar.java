package de.game.gui.elements;

import java.awt.Color;

import de.game.gui.GuiScreen;

public class GuiScrollBar extends GuiElement {
	
	public int x, y;
	public int height;
	public boolean isPressed;
	public float scrollState;
	public int exactScrollState;
	public Color color;
	public boolean isHorizontal;
	
	public GuiScrollBar(int posX, int posY, int height, Color color, boolean horizontal) {
		
		this.height = height;
		this.x = posX;
		this.y = posY;
		this.color = color;
		this.isHorizontal = horizontal;
		this.exactScrollState = isHorizontal ? x : y;
		
	}
	
	@Override
	public void draw() {
		
		if (isHorizontal) {

			GuiScreen.drawElementQuad(x, y, height, 20, color);
			
			int scrollY = exactScrollState;
			
			GuiScreen.drawTexture(scrollY, y, 20, 20, "textures/misc/list_scrollbar_horizontal.png");
			
		} else {
			
			GuiScreen.drawElementQuad(x, y, 20, height, color);
			
			int scrollY = exactScrollState;
			
			GuiScreen.drawTexture(x, scrollY, 20, 20, "gui/scrollbar");
			
		}
		
	}
	
	@Override
	public void onPressed(float mouseX, float mouseY) {
		

		if (isHorizontal) {
			if (mouseX > x && mouseX < x + height && mouseY > y && mouseY < y + 20) {
				updateScrollBarH(mouseX);
			}
		} else {
			if (mouseX > x && mouseX < x + 20 && mouseY > y && mouseY < y + height) {
				updateScrollBar(mouseY);
			}
		}
		
		isPressed = true;
		
	}
	
	@Override
	public boolean isMouseOver(float mouseX, float mouseY) {
		
		boolean flag1 = (mouseX >= x && mouseX <= 20 + x) && (mouseY >= y && mouseY <= height + y);
		boolean flag2 = (mouseX >= x && mouseX <= height + x) && (mouseY >= y && mouseY <= 20 + y);
		
		return isHorizontal ? flag2 : flag1;
		
	}

	@Override
	public int getWidth() {
		return isHorizontal ? height : 5;
	}

	@Override
	public int getHeight() {
		return isHorizontal ? 5 : height;
	}
	
	public float getScrollState() {
		return scrollState;
	}
	
	public void updateScrollBar(float mouseY) {
		
		int clickHeight = (int) Math.max(Math.min(mouseY, y + height - 10) - (y + 10), 0);
		float scrollState = clickHeight / (height - 20F);
		int exactState = (int) Math.max(y, Math.min(y + height - 20, mouseY - 10));
		
		if (isPressed && this.exactScrollState != exactState) {
			this.scrollState = scrollState;
			this.exactScrollState = exactState;
		}
		
	}
	
	public void updateScrollBarH(float mouseX) {
		
		int clickHeight = (int) Math.max(Math.min(mouseX, x + height - 20) - (x + 20), 0);
		float scrollState = clickHeight / (height - 30F);
		int exactState = (int) Math.max(x, Math.min(x + height - 20, mouseX - 20));
		
		if (isPressed && this.exactScrollState != exactState) {
			this.scrollState = scrollState;
			this.exactScrollState = exactState;
		}
		
	}
	
	@Override
	public void onReleased(float mouseX, float mouseY) {
		isPressed = false;
	}
	
}
