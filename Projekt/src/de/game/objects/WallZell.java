package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class WallZell extends Zell {

	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		drawTexture("zells/wall_zell");
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		return false;
	}

	@Override
	public String getParticleTexture() {
		return "zells/wall_zell";
	}

}
