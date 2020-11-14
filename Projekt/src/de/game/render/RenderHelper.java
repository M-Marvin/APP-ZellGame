package de.game.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

public class RenderHelper {
    
    public static void renderTexture(float x, float y, float width, float height, TextureIO tex) {
    	
		GL11.glPushMatrix();
		tex.bind();
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x + width, y);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		tex.unbind();
		GL11.glPopMatrix();
    	
    }
    
    public static void renderTextureStretch(float x, float y, float width, float height, TextureIO tex) {
		
		GL11.glPushMatrix();

		GL11.glColor3f(1, 1, 1);
		GL11.glTranslatef(x, y, 0);
		
		float hw = -width / 2;
		float hw1 = width / 2;
		float hh = -height / 2;
		float hh1 = height / 2;
		

		float w = (x) / tex.getWidth() - 0.5F;
		float h = (y) / tex.getHeight() - 0.5F;
		float w1 = (x + width) / tex.getWidth() - 0.5F;
		float h1 = (y + height) / tex.getHeight() - 0.5F;
				
		tex.bind();
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(w, h);
		GL11.glVertex2f(hw, hh);
		GL11.glTexCoord2f(w, h1);
		GL11.glVertex2f(hw, hh1);
		GL11.glTexCoord2f(w1, h1);
		GL11.glVertex2f(hw1, hh1);
		GL11.glTexCoord2f(w1, h);
		GL11.glVertex2f(hw1, hh);
		
		GL11.glEnd();
		tex.unbind();
		
		GL11.glPopMatrix();
		
    }
    
    public static void renderElementQuad(float x, float y, float width, float height, Color color) {
    	
    	GL11.glPushMatrix();
    	{
    		
    		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    		
    		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
    		
    		GL11.glVertex2f(x, y);
    		GL11.glVertex2f(x + width, y);
    		GL11.glVertex2f(x, y + height);
    		GL11.glVertex2f(x + width, y + height);
    		
    		GL11.glEnd();
    		
    		GL11.glColor3f(1, 1, 1);
    		
    	}
    	GL11.glPopMatrix();
    	
    }
    
    public static void renderElementLine(float x1, float y1, float x2, float y2, Color color) {
    	
    	GL11.glPushMatrix();
    	{
    		
    		GL11.glEnable(GL11.GL_LINE_WIDTH);
    		GL11.glLineWidth(2);
    		
    		GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
			
    		GL11.glBegin(GL11.GL_LINES);
    		GL11.glVertex2f(x1, y1);
    		GL11.glVertex2f(x2, y2);
    		GL11.glEnd();

			GL11.glDisable(GL11.GL_LINE_WIDTH);
			GL11.glLineWidth(1);
			
			GL11.glColor3f(1F, 1F, 1F);

    	}
    	GL11.glPopMatrix();
    	
    }
    
}
