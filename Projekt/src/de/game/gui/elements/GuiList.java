package de.game.gui.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import de.game.Engine;
import de.game.gui.GuiScreen;

public class GuiList<T extends IGuiListListable> extends GuiElement {
	
	public int x, y;
	public int width;
	public int height;
	public List<T> items;
	public int clickedItem;
	public int hoverItem;
	public boolean isMouseOver;
	public boolean isPressed;
	public int scrollState;
	public int exactScrollState;
	public Color color;
	
	public GuiList(int posX, int posY, int width, int height, Color color) {
		
		this.height = height;
		this.width = width;
		this.x = posX;
		this.y = posY;
		this.exactScrollState = y;
		this.color = color;
		this.items = new ArrayList<>();
		clickedItem = -1;
		
	}
	
	public void selectItem(T item) {
		
		for (int i = 0; i < items.size(); i++) {
			
			T entry = items.get(i);
			
			if (entry.equals(item)) {
				
				clickedItem = i;
				return;
				
			}
			
		}
		
		clickedItem = -1;
		
	}
	
	@Override
	public void draw() {
		
		GuiScreen.drawElementQuad(x, y, width, height, color);
		
		int visible_items = 0;
		
		if (items.size() != 0) {
			visible_items = height / (items.get(0) != null ? items.get(0).getHight() : 0);
		}

		int scrollY = exactScrollState;
		
		GuiScreen.drawTexture(x + width - 20, scrollY, 20, 20, "misc/list_scrollbar");
		
		
		GuiScreen.drawElementLine(x + width - 20, y, x + width - 20, y + height, color);
		
		for (int i = 0; i < Math.min(visible_items, items.size()); i++) {
			
			T item = items.get(scrollState + i);
			
			boolean selected = clickedItem == scrollState + i;
			
			item.draw(x, y + i * item.getHight(), width - 5, selected);
			
			GuiScreen.drawElementLine(x + 2, y + (i + 1) * item.getHight(), x + width - 30, y + (i + 1) * item.getHight(), this.color);
			
		}
		
		if (hoverItem > -1) {
			
			if (items.size() >= 1) GuiScreen.drawElementQuad(x, y + hoverItem * items.get(0).getHight(), width - 20, items.get(0).getHight(), new Color(0, 142, 142, 50));
			
		}
		
	}
	
	@Override
	public void onPressed(float mouseX, float mouseY) {
		
		int i = findClickedItem(new Vector2f(mouseX, mouseY));
		
		if (!isPressed) {
			
			if (i != -1) {
				
				if (clickedItem == i) {
					
					clickedItem = -1;
					
				} else {

					clickedItem = i;
					
				}
				
				Engine.getInstance().getSoundPlayer().playSound("list_select");
				
			}
			
			isPressed = true;
			
		}
		
		if (mouseX > x + width - 20 && mouseX < x + width && mouseY > y && mouseY < y + height) {
			updateScrollBar(mouseY);
		}
		
	}
	
	@Override
	public void onReleased(float mouseX, float mouseY) {
		isPressed = false;
	}

	public T getSelectedItem() {
		return (clickedItem > -1 && clickedItem < items.size()) ? items.get(clickedItem) : null;
	}
	
	public void addItemToList(T item) {
		if (!this.items.contains(item)) items.add(item);
	}
	
	public void removeItemFromList(int index) {
		if (this.items.size() > index) this.items.remove(index);
	}
	public void removeItemFromList(T item) {
		this.items.remove(item);
	}
	
	public void clearList() {
		this.items.clear();
	}
	
	public List<T> getEntrys() {
		return items;
	}
	
	@Override
	public boolean isMouseOver(float mouseX, float mouseY) {
		
		isMouseOver = (mouseX >= x && mouseX <= width + x) && (mouseY >= y && mouseY <= height + y);
		
		int i = findClickedItem(new Vector2f(mouseX, mouseY)) - scrollState;
		
		hoverItem = i;
		
		return isMouseOver;
		
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
	
	public int findClickedItem(Vector2f clickPos) {
		
		int visible_items = 0;
		
		if (items.size() > 0) {
			visible_items = height / (items.get(0) != null ? items.get(0).getHight() : 0);
		}
		
		for (int i = scrollState; i < Math.min(visible_items + scrollState, items.size()); i++) {
			
			int x = this.x;
			int y = this.y + (i - scrollState) * items.get(i).getHight();
			int width = this.width - 10;
			int height = items.get(i).getHight();
				
			if (clickPos.x > x && clickPos.x < x + width && clickPos.y > y && clickPos.y < y + height) {
				
				return i;
				
			}
			
		}
		
		return -1;
		
	}
	
	public void updateScrollBar(float mouseY) {
		
		int visible_items = 0;
		
		if (items.size() > 0) {
			visible_items = height / (items.get(0) != null ? items.get(0).getHight() : 0);
		}
		
		int scrollResolution = Math.max(0, items.size() - visible_items);
		
		int clickHeight = (int) Math.max(Math.min(mouseY, y + height - 10) - (y + 10), 0);
		int scrollState = (int) ((clickHeight / (height - 10F)) * scrollResolution);
		int exactState = (int) Math.max(y, Math.min(y + height - 20, mouseY - 20));
		
		if (isPressed && this.exactScrollState != exactState) {
			this.scrollState = scrollState;
			this.exactScrollState = exactState;
		}
		
	}
	
}
