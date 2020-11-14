package de.game.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class StringRenderer {
	
	public static void drawString(String text, float x, float y, float scale, Color color, TextureIO tex) {
		
		char[] chars = text.toCharArray();
		int width = 0;
		
		GL11.glPushMatrix();
		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
		
		for (char c : chars) {
			
			int posX = c % 16;
			int posY = c / 16;
			posX *= 8;
			posY *= 8;
			posY = 128 - posY;
			float ax = posX / 128F;
			float ay = posY / 128F; 
			float bx = (posX + 8) / 128F;
			float by = (posY - 8) / 128F;
			
			tex.bind();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(ax, by);
			GL11.glVertex2f(x + width, y);
			GL11.glTexCoord2f(bx, by);
			GL11.glVertex2f(x + scale + width, y);
			GL11.glTexCoord2f(bx, ay);
			GL11.glVertex2f(x + scale + width, y + scale);
			GL11.glTexCoord2f(ax, ay);
			GL11.glVertex2f(x + width, y + scale);
			GL11.glEnd();
			tex.unbind();
			
			width += scale;
			
		}

		GL11.glColor3f(1, 1, 1);
		
		GL11.glPopMatrix();
		
	}
	
	public static String cutString(String text, float scale, int width) {
		
		char[] chars = text.toCharArray();
		char[] newString = new char[(int) Math.min(chars.length, width / scale)];
		
		for (int i = 0; i < newString.length; i++) {
			newString[i] = chars[i];
		}
		
		return new String(newString);
		
	}
	
}
