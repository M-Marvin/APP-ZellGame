package de.game.particles;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import de.game.gamelogic.Particle;
import de.game.util.Vec2;

public class ParticleExplodingZell extends Particle {
	
	protected Color color;
	protected Vec2 size;
	
	public ParticleExplodingZell(Vec2 pos, Random rand, Color color) {
		super(pos, rand, 40, 40);
		this.color = color;
		this.size = new Vec2(rand.nextInt(5), rand.nextInt(5));
	}

	@Override
	public void draw() {
		
		GL11.glPushMatrix();
		GL11.glColor3f(this.color.getRed() / 255F, this.color.getGreen(), this.color.getBlue());
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(pos.x, pos.y);
		GL11.glVertex2f(pos.x + size.x, pos.y);
		GL11.glVertex2f(pos.x + size.x, pos.y + size.y);
		GL11.glVertex2f(pos.x, pos.y + size.y);
		GL11.glEnd();
		GL11.glPopMatrix();
		
	}
	
}
