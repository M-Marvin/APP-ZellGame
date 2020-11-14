package de.game.gui.elements;

import java.awt.Color;

import de.game.gui.GuiScreen;

public class List_SettingEntry<T extends Object> implements IGuiListListable {
	
	protected String name;
	protected T value;
	
	public List_SettingEntry(String name, T value) {
		this.value = value;
		this.name = name;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void draw(int x, int y, int width, boolean selected) {
		
		String value = this.value.toString();
		
		Color color = !selected ? new Color(255, 255, 255) : new Color(0, 255, 0);
		GuiScreen.drawString(x + 4, y + 0.5F, name, 8, color, width / 2);
		GuiScreen.drawString(x + 4 + width / 2, y + 0.5F, "| " + value, 8, color, width / 2);
		
	}
	
	@Override
	public int getHight() {
		return 20;
	}
	
}
