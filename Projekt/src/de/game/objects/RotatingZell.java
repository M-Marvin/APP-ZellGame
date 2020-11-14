package de.game.objects;

import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.gamelogic.zellpropertys.ZPropertyRotDirection;
import de.game.util.Direction;
import de.game.util.RotDirection;
import de.game.util.Vec2i;

public class RotatingZell extends Zell {
	
	public static final ZPropertyRotDirection ROTATION = new ZPropertyRotDirection("rotation");
	
	@Override
	public void draw(World world, Vec2i pos, ZellState state) {
		RotDirection rot = state.getProperty(ROTATION);
		drawTexture("zells/rotating_zell_" + (rot == RotDirection.CW ? "cw" : "ccw"));
	}

	@Override
	public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
		return true;
	}
	
	@Override
	public boolean canBeRotated(ZellState state) {
		return false;
	}
	
	@Override
	public void stepAkt(World world, Vec2i pos, ZellState state) {
		
		RotDirection rotation = state.getProperty(ROTATION);
		world.getZellState(pos.offset(Direction.LEFT)).rotate(rotation);
		world.getZellState(pos.offset(Direction.RIGHT)).rotate(rotation);
		world.getZellState(pos.offset(Direction.UP)).rotate(rotation);
		world.getZellState(pos.offset(Direction.DOWN)).rotate(rotation);
		
	}
	
	@Override
	public ZellState createState() {
		return new ZellState(this, new ZProperty[] {ROTATION});
	}

	@Override
	public String getParticleTexture() {
		return "zells/rotating_zell";
	}

}
