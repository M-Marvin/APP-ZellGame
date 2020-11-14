package de.game.gamelogic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import de.game.render.RenderHelper;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class World {
	
	protected Random random;
	protected HashMap<Vec2i, ZellState> zells;
	protected List<Particle> particles;
	protected List<Vec2i> editeableZells;
	
	public World(LevelDef level) {
		
		this.random = new Random();
		this.zells = new HashMap<Vec2i, ZellState>();
		this.particles = new ArrayList<Particle>();
		
		for (Entry<Vec2i, ZellState> zell : level.zells.entrySet()) {
			
			ZellState state = zell.getValue() == null ? ZellState.EMPTY : zell.getValue();
			this.zells.put(zell.getKey(), state.copy());
			
		}
		
		this.editeableZells = level.editeableZells;
		
	}
	
	public LevelDef getLevelDef() {
		
		LevelDef level = new LevelDef();
		level.editeableZells = this.editeableZells.subList(0, this.editeableZells.size());
		for (Entry<Vec2i, ZellState> entry : this.zells.entrySet()) {
			level.zells.put(entry.getKey(), entry.getValue() != ZellState.EMPTY.copy() ? entry.getValue().copy() : null);
		}
		return level;
		
	}
	
	public boolean isEditeable(Vec2i pos) {
		return this.editeableZells.contains(pos);
	}
	
	public void setEditeable(Vec2i pos, boolean editeable) {
		if (editeable && !this.editeableZells.contains(pos)) {
			this.editeableZells.add(pos);
		} else {
			this.editeableZells.remove(pos);
		}
	}
	
	public void setZellState(Vec2i pos, ZellState state) {
		if (this.zells.containsKey(pos)) {
			ZellState stateR = zells.remove(pos);
			zells.put(pos, state);
			stateR.getZell().onReplace(this, pos, stateR, state);
			
		}
	}
	
	public void removeZellState(Vec2i pos) {
		if (this.zells.containsKey(pos)) {
			zells.put(pos, ZellState.EMPTY);	
		}
	}
	
	public ZellState getZellState(Vec2i pos) {
		if (this.zells.containsKey(pos)) {
			ZellState state = zells.get(pos);
			return state == null ? ZellState.EMPTY : state;
		}
		return ZellState.EMPTY;
	}
	
	public void moveZellState(Vec2i pos, Direction direction) {
		
		Vec2i pos2 = pos.offset(direction);
		if (this.zells.containsKey(pos2)) {
			
			ZellState state = getZellState(pos);
			setZellState(pos2, state);
			removeZellState(pos);
			state.startAnimation(pos, pos2);
			
		}
		
	}
	
	public boolean isInWorld(Vec2i pos) {
		return zells.containsKey(pos);
	}
	
	public void setInWorld(Vec2i pos, boolean inWorld) {
		
		if (!pos.isNegativ()) {
			if (inWorld) {
				this.zells.put(pos, ZellState.EMPTY);
			} else {
				this.zells.remove(pos);
			}
		}
		
	}
	
	public Vec2i[] canMoveZells(Vec2i pos, Direction direction) {
		
		List<Vec2i> zellsToMove = new ArrayList<Vec2i>();
		
		int index = 1;
		while (index < 1000) {
			
			Vec2i pos2 = pos.offset(direction, index++);
			ZellState state = getZellState(pos2);
			Zell zell = state.getZell();
			
			if (!isInWorld(pos2)) return null;
			if (state.isAnimationStarting()) return null;
			if (state.isRotating()) return null;
			if (state.getZell() == Zell.EMPTY) return zellsToMove.toArray(new Vec2i[] {});
			if (!zell.canBePushed(this, pos2, state, direction)) {
				if (zell.isReplaceable(this, pos2, state, direction)) {
					return zellsToMove.toArray(new Vec2i[] {});
				} else {
					return null;
				}
			}
			
			zellsToMove.add(pos2);
			
		}
		
		return null;
		
	}
	
	public Vec2i[] getZellsInLine(Vec2i pos, Direction direction) {
		
		List<Vec2i> zellsToMove = new ArrayList<Vec2i>();
		
		int index = 1;
		while (index < 1000) {
			
			Vec2i pos2 = pos.offset(direction, index++);
			ZellState state = getZellState(pos2);
			
			if (!isInWorld(pos2)) return zellsToMove.toArray(new Vec2i[] {});
			if (state.isEmpty()) return zellsToMove.toArray(new Vec2i[] {});
			
			zellsToMove.add(pos2);
			
		}
		
		return zellsToMove.toArray(new Vec2i[] {});
		
	}
	
	public Random getRandom() {
		return this.random;
	}
	
	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}
	
	public void draw() {
		
		for (Vec2i pos : zells.keySet()) {
			
			Color color = isEditeable(pos) ? new Color(40, 40, 40, 125) : new Color(0, 0, 0, 125);
			RenderHelper.renderElementQuad(pos.x * 20 + 1, pos.y * 20 + 1, 20 - 2, 20 - 2, color);
			
		}
		
		for (Entry<Vec2i, ZellState> entry : zells.entrySet()) {
			
			Vec2i pos = entry.getKey();
			ZellState state = entry.getValue();
			
			if (state.getZell() != Zell.EMPTY) {
				
				float zx = pos.x * 20 + state.getCurentAnimationPos().x;
				float zy = pos.y * 20 + state.getCurentAnimationPos().y;
				
				GL11.glPushMatrix();
				GL11.glTranslatef(zx, zy, 0);
				Zell zell = state.getZell();
				zell.draw(this, pos, state);
				GL11.glPopMatrix();
				
			}
			
		}
				
		for (Particle particle : this.particles.toArray(new Particle[] {})) {
			particle.draw();
		}
		
	}

	public void tick(boolean doNextStep) {
		
		boolean canStep = doNextStep;
		for (Entry<Vec2i, ZellState> entry : zells.entrySet()) {
			
			ZellState state = entry.getValue();
			Zell zell = state.getZell();
			
			if (zell != Zell.EMPTY) {
				
				boolean animationComplete = state.tickAnimation();
				
				if (!animationComplete) canStep = false;
				
			}
			
		}
		
		if (canStep) {
						
			Zell[] typs = GameRegistry.getRegistredZells();
			ZellState[] states = this.zells.values().toArray(new ZellState[] {});
			
			for (Zell type : typs) {
				for (ZellState state : states) {
					
					if (type == state.getZell() && state.getZell() != Zell.EMPTY) {
						
						Vec2i pos = getPosForState(state);
						Zell zell = state.getZell();
						
						if (zell != Zell.EMPTY) {
							
							if (pos != null) zell.step(this, pos, state);
							
						}
						
					}
					
				}
			}
			
			for (Zell type : typs) {
				
				for (ZellState state : states) {
					
					if (type == state.getZell() && state.getZell() != Zell.EMPTY) {
						
						Vec2i pos = getPosForState(state);
						Zell zell = state.getZell();
						
						if (zell != Zell.EMPTY) {
							
							if (pos != null) zell.stepAkt(this, pos, state);
							
						}
						
					}
					
				}
			}
			
		}
		
		for (Particle particle : this.particles.toArray(new Particle[] {})) {
			particle.tick();
			if (!particle.isAlife()) particles.remove(particle);
		}
		
	}
	
	private Vec2i getPosForState(ZellState state) {
		for (Entry<Vec2i, ZellState> entry : this.zells.entrySet()) {
			if (entry.getValue() == state) return entry.getKey();
		}
		return null;
	}
	
}
