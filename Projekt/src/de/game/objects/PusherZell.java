package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.gamelogic.zellpropertys.ZPropertyDirection;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class PusherZell extends Zell {
	
	public static final ZPropertyDirection DIRECTION = new ZPropertyDirection("direction");
	
	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		Direction direction = state.getProperty(DIRECTION);
		int rot = direction.getRotation() + state.getCurentAnimationRot();
		drawTexture("zells/pusher_zell", rot);
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		Direction facing = state.getProperty(DIRECTION);
		return direction != facing.getOpposite();
	}

	@Override
	public boolean canBeRotated(ZellState state) {
		return true;
	}
	
	@Override
	public void step(World world, Vec2i pos, ZellState state) {
		
		Direction direction = state.getProperty(DIRECTION);
		Vec2i[] zellsToMove = world.canMoveZells(pos, direction);
		
		if (zellsToMove != null && !state.isAnimationStarting()) {
			
			// Checks if this pusher is already pushed by another
			Vec2i[] zellsBack = world.getZellsInLine(pos, direction.getOpposite());
			for (Vec2i pos1 : zellsBack) {
				ZellState zell1 = world.getZellState(pos1);
				if (zell1.getZell() == this) {
					Direction facing = zell1.getProperty(DIRECTION);
					if (facing == direction && !zell1.isAnimationStarting()) {
						return;
					}
				}
				if (zell1.isEmpty()) break;
			}
			
			for (Vec2i pos2 : zellsToMove) {
				ZellState zell2 = world.getZellState(pos2);
				if (zell2.getZell() == this) {
					Direction facing = zell2.getProperty(DIRECTION);
					if (facing == direction.getOpposite()) return;
				}
			}
			
			for (int i = zellsToMove.length - 1; i >= 0; i--) {
				world.moveZellState(zellsToMove[i], direction);
			}
			
			world.moveZellState(pos, direction);
			
		}
		
	}
		
	@Override
	public ZellState createState() {
		return new ZellState(this, new ZProperty[] {DIRECTION});
	}

	@Override
	public String getParticleTexture() {
		return "zells/pusher_zell";
	}

}
