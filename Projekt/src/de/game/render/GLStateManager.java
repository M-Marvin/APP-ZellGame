package de.game.render;

import org.lwjgl.opengl.GL11;

public class GLStateManager {
	
	public static final float defineNear = 0.0F;
	public static final float defineFar = 1.0F;
	
	public static void reconfigurateOrtho(int width, int height) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, defineNear, defineFar);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
}
