package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.gamelogic.zellpropertys.ZPropertyDirection;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class SlidingZell extends Zell {
	
	public static final ZPropertyDirection DIRECTION = new ZPropertyDirection("direction");
	
	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		Direction direction = state.getProperty(DIRECTION);
		int rot = direction.getRotation() + state.getCurentAnimationRot();
		drawTexture("zells/sliding_zell", rot);
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		Direction axis = state.getProperty(DIRECTION);
		return direction == axis || direction == axis.getOpposite();
	}

	@Override
	public boolean canBeRotated(ZellState state) {
		return true;
	}
	
	@Override
	public ZellState createState() {
		return new ZellState(this, new ZProperty[] {DIRECTION});
	}

	@Override
	public String getParticleTexture() {
		return "zells/sliding_zell";
	}

}
