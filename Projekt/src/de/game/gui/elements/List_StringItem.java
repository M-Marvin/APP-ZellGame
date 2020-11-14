package de.game.gui.elements;

import java.awt.Color;

import de.game.gui.GuiScreen;

public class List_StringItem implements IGuiListListable {

	String string;
	
	public List_StringItem(String string) {
		this.string = string;
	}
	
	@Override
	public void draw(int x, int y, int width, boolean selected) {
		GuiScreen.drawString(x + 4, y + 2, string, 28, selected ? new Color(0, 255, 0) : new Color(255, 255, 255), 900);
	}

	@Override
	public int getHight() {
		return 30;
	}
	
	public String getString() {
		return string;
	}

}
