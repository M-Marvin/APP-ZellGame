package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.gamelogic.zellpropertys.ZPropertyDirection;
import de.game.util.Direction;
import de.game.util.Vec2i;

public class DuplicatorZell extends Zell {
	
	public static final ZPropertyDirection DIRECTION = new ZPropertyDirection("direction");
	
	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		Direction direction = state.getProperty(DIRECTION);
		int rot = direction.getRotation() + state.getCurentAnimationRot();
		drawTexture("zells/duplicator_zell", rot);
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		return true;
	}

	@Override
	public boolean canBeRotated(ZellState state) {
		return true;
	}
	
	@Override
	public void step(World world, Vec2i pos, ZellState state) {
		
		Direction direction = state.getProperty(DIRECTION);
		ZellState stateD = world.getZellState(pos.offset(direction.getOpposite()));
		
		if (!stateD.isEmpty()) {
			
			ZellState stateN = stateD.copy();
			
			Vec2i[] zellsToMove = world.canMoveZells(pos, direction);
			
			if (zellsToMove != null && !state.isAnimationStarting()) {
				
				for (int i = zellsToMove.length - 1; i >= 0; i--) {
					world.moveZellState(zellsToMove[i], direction);
				}
				
				stateN.startAnimation(pos, pos.offset(direction));
				world.setZellState(pos.offset(direction), stateN);
				
			}
			
		}
		
	}
	
	@Override
	public ZellState createState() {
		return new ZellState(this, new ZProperty[] {DIRECTION});
	}

	@Override
	public String getParticleTexture() {
		return "zells/duplicator_zell";
	}

}
