package de.game.gamelogic;

import de.game.util.Vec2;

public abstract class Particle {
	
	protected Vec2 pos;
	protected Vec2 velocity;
	protected int lifeTime;
		
	public Particle(Vec2 pos, java.util.Random rand, int force, int maxLifeTime) {
		this.pos = pos;
		this.lifeTime = rand.nextInt(maxLifeTime);
		float vx = rand.nextInt(force) * (rand.nextBoolean() ? -1 : 1);
		float vy = rand.nextInt(force) * (rand.nextBoolean() ? -1 : 1);
		this.velocity = new Vec2(vx, vy);
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public Vec2 getVelocity() {
		return velocity;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public boolean isAlife() {
		return this.lifeTime > 0;
	}
	
	public void tick() {
		this.pos.x += this.velocity.x;
		this.pos.y += this.velocity.y;
		this.velocity.x *= 0.9F;
		this.velocity.y *= 0.9F;
		this.lifeTime--;
	}
	
	public abstract void draw();
	
}
