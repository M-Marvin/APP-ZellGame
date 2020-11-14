package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class PusheableZell extends Zell {
	
	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		drawTexture("zells/pusheable_zell");
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		return true;
	}

	@Override
	public String getParticleTexture() {
		return "zells/pusheable_zell";
	}

}
