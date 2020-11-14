package de.game.objects;

import java.awt.Color;

import de.game.Engine;
import de.game.gamelogic.Particle;
import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.particles.ParticleExplodingZell;
import de.game.util.Direction;
import de.game.util.Vec2;
import de.game.util.Vec2i;

public class ExplosivZell extends Zell {

	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		drawTexture("zells/explosiv_zell");
	}
	
	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		return false;
	}
	
	@Override
	public boolean isReplaceable(World world, Vec2i pos, ZellState state, Direction direction) {
		return true;
	}
	
	@Override
	public void onReplace(World world, Vec2i pos, ZellState state, ZellState newState) {
		
		world.removeZellState(pos);
		
		for (int i = 0; i < 100; i++) {
			
			Particle particle = new ParticleExplodingZell(new Vec2(pos.x * 20, pos.y * 20), world.getRandom(), new Color(244, 0, 0));
			world.addParticle(particle);
			
		}
		
		Engine.getInstance().getSoundPlayer().playSound("sfx/explosion");
		
	}

	@Override
	public String getParticleTexture() {
		return "zells/explosiv_zell";
	}

}
