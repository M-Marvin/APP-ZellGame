package de.game.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.input.Mouse;

import de.game.Engine;
import de.game.gui.elements.GuiElement;
import de.game.gui.elements.ITickeable;
import de.game.render.RenderHelper;
import de.game.render.StringRenderer;
import de.game.render.TextureIO;
import de.game.util.ResourceLoader;

public abstract class GuiScreen {
	
	public static final String GUI_BACKGROUND = "gui/background";
	public static final int DEFAULT_STRING_SIZE = 15;
	
	public List<GuiElement> guiElements;
	
	public void onGuiOpen() {
		
	}
	
	public void onGuiClosed() {
		
		guiElements.clear();
		
	}
	
	public void renderBackground() {
		
		drawTexture(0, 0, 1000, 600, GUI_BACKGROUND);
		
	}
	
	public void renderForground() {
		
		for (GuiElement element : this.guiElements) {
			if (element.isEnabled()) element.draw();
		}
		
	}
	
	public GuiScreen() {
		guiElements = new ArrayList<>();
	}
	
	public boolean updateTick() {
		
		float mx = Engine.getInstance().getInputHandler().getMouseX();
		float my = Engine.getInstance().getInputHandler().getMouseY();
		
		if (Engine.getInstance().getInputHandler().isTyped("control.escape")) {
			this.onPressEscape();
		}
		
		if (isOverGui(mx, my)) {
			
			try {
				
				for (GuiElement element : guiElements) {
					
					if (element.isEnabled()) {
						
						if (element instanceof ITickeable) ((ITickeable) element).updateTick();
						
						boolean flag = element.isMouseOver(mx, my);
						
						if (Mouse.isButtonDown(0) && flag) {
							element.onPressed(mx, my);
						} else {
							element.onReleased(mx, my);
						}
						
					}
					
				}
				
			} catch (ConcurrentModificationException e) {}
			
			return true;
			
		} else {
			
			return false;
			
		}
		
	}
	
	public abstract boolean isOverGui(float x, float y);
    
    public abstract void onPressEscape();
	
	public static void drawString(float x, float y, String text) {
		
		ResourceLoader loader = Engine.getInstance().getResourceLoader();
		StringRenderer.drawString(text, x, y, DEFAULT_STRING_SIZE, new Color(255, 255, 255), loader.getTexture("misc/asci_code"));
		
	}
	
	public static void drawString(float x, float y, String text, Color color) {
		
		ResourceLoader loader = Engine.getInstance().getResourceLoader();
		StringRenderer.drawString(text, x, y, DEFAULT_STRING_SIZE, color, loader.getTexture("misc/asci_code"));
		
	}
	
	public static void drawString(float x, float y, String text, Color color, int maxWidth) {
		
		ResourceLoader loader = Engine.getInstance().getResourceLoader();
		String s = StringRenderer.cutString(text, DEFAULT_STRING_SIZE, maxWidth);
		StringRenderer.drawString(s, x, y, DEFAULT_STRING_SIZE, color, loader.getTexture("misc/asci_code"));
		
	}
	
	public static void drawString(float x, float y, String text, float scale, Color color, int maxWidth) {
		
		ResourceLoader loader = Engine.getInstance().getResourceLoader();
		String s = StringRenderer.cutString(text, scale, maxWidth);
		StringRenderer.drawString(s, x, y, scale, color, loader.getTexture("misc/asci_code"));
		
	}
	
	public static void drawElementLine(float x1, float y1, float x2, float y2, Color color) {
		
		RenderHelper.renderElementLine(x1, y1, x2, y2, color);
		
	}
	
	public static void drawElementQuad(float x, float y, float width, float height, Color color) {
		
		RenderHelper.renderElementQuad(x, y, width, height, color);
		
	}
	
	public static void drawString(float x, float y, String text, float scale) {
		
		ResourceLoader loader = Engine.getInstance().getResourceLoader();
		StringRenderer.drawString(text, x, y, scale, new Color(255, 255, 255), loader.getTexture("misc/asci_code"));
		
	}
	
	public static void drawTexture(float x, float y, float width, float height, String res) {
		
		TextureIO tex = Engine.getInstance().getResourceLoader().getTexture(res);
		RenderHelper.renderTexture(x, y, width, height, tex);
		
	}
	
	public static void drawTexture(float x, float y, float width, float height, TextureIO tex) {
		
		RenderHelper.renderTexture(x, y, width, height, tex);
		
	}
	
	public abstract void init(Object... args);
	
}
