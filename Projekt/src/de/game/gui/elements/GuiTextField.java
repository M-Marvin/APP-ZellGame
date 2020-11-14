//package de.game.gui.elements;
//
//import java.awt.Color;
//
//import org.lwjgl.input.Keyboard;
//
//import de.game.gui.GuiScreen;
//
//public class GuiTextField extends GuiElement implements ITickeable {
//	
//	public int x;
//	public int y;
//	public int width;
//	public String text;
//	public boolean focused;
//	public int cursorPosition;
//	private int maxLength;
//	private int preInit;
//	private String initText;
//	
//	public GuiTextField(int x, int y, int width, String startText) {
//		this.x = x;
//		this.y = y;
//		this.width = width;
//		this.text = startText;
//		this.initText = startText;
//		this.preInit = 2;
//	}
//	
//	@Override
//	public void draw() {
//
//		GuiScreen.drawElementQuad(x, y, width, 16, new Color(0, 0, 0, 150), new Color(255, 255, 255));
//		
//		GuiScreen.drawString(x + 3, y + 4, text);
//		
//		if (focused) {
//			
//			int cx = x + 1 + cursorPosition * 9;
//			int cy = y + 3;
//			
//			GuiScreen.drawElementLine(cx, cy, cx, cy + 9, new Color(255, 255, 255));
//			
//		}
//		
//	}
//	
//	@Override
//	public void onPressed(float mouseX, float mouseY) {
//		focused = true;
//	}
//
//	@Override
//	public void onReleased(float mouseX, float mouseY) {}
//
//	@Override
//	public boolean isMouseOver(float mouseX, float mouseY) {
//		
//		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + 16;
//		
//	}
//
//	@Override
//	public int getWidth() {
//		return this.width;
//	}
//
//	@Override
//	public int getHeight() {
//		return 16;
//	}
//	
//	public String getText() {
//		return this.text;
//	}
//	
//	@Override
//	public void updateTick() {
//		
//		maxLength = width / 9;
//				
//		while (Keyboard.next()) {
//			
//			if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
//                this.text = "";
//                cursorPosition = 0;
//            } else if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && Keyboard.getEventKeyState()) {
//                try {
//                    this.text = this.text.substring(0, cursorPosition - 1) + this.text.substring(cursorPosition, this.text.length());
//                    cursorPosition--;
//                } catch (StringIndexOutOfBoundsException e) {}
//            } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//            	cursorPosition = Math.max(0, cursorPosition - 1);
//            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//				cursorPosition = Math.min(text.length(), cursorPosition + 1);
//			} else if (Keyboard.getEventKeyState() && isAllowedKey() && this.text.length() < maxLength) {
//                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
//                	this.text = this.text.substring(0, cursorPosition ) + Character.toUpperCase(Keyboard.getEventCharacter()) + this.text.substring(cursorPosition, this.text.length());
//                    cursorPosition++;
//                } else {
//                	this.text = this.text.substring(0, cursorPosition ) + String.valueOf(Keyboard.getEventCharacter()) + this.text.substring(cursorPosition, this.text.length());
//                    cursorPosition++;
//                }
//            }
//			
//        }
//		
//		if (preInit > 0) {
//			this.text = initText;
//			this.cursorPosition = text.length();
//			preInit--;
//		}
//	}
//	
//	private boolean isAllowedKey() {
//		return  Keyboard.getEventKey() != Keyboard.KEY_LSHIFT && 
//				Keyboard.getEventKey() != Keyboard.KEY_RSHIFT &&
//				Keyboard.getEventKey() != Keyboard.KEY_LEFT &&
//				Keyboard.getEventKey() != Keyboard.KEY_RIGHT;
//	}
//	
//}
