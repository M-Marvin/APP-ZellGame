package de.game.gamelogic;

import org.lwjgl.opengl.GL11;

import de.game.Engine;
import de.game.gamelogic.zellpropertys.ZProperty;
import de.game.gamelogic.zellpropertys.ZPropertyDirection;
import de.game.render.RenderHelper;
import de.game.render.TextureIO;
import de.game.util.Direction;
import de.game.util.RotDirection;
import de.game.util.Vec2;
import de.game.util.Vec2i;

public abstract class Zell {
	
	public static final Zell EMPTY = new Zell() {
		@Override
		public void draw(World world, Vec2i pos, ZellState state) {}
		@Override
		public boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction) {
			return false;
		}
		@Override
		public String getParticleTexture() {
			return "";
		}
	};
	
	public Zell() {
		
	}
	
	public abstract void draw(World world, Vec2i pos, ZellState state);
	
	public abstract boolean canBePushed(World world, Vec2i pos, ZellState state, Direction direction);
	
	public boolean isReplaceable(World world, Vec2i pos, ZellState state, Direction direction) {
		return false;
	}
	
	public void onReplace(World world, Vec2i pos, ZellState state, ZellState newState) {}
	
	public boolean canBeRotated(ZellState state) {
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public void onRotate(RotDirection direction, ZellState state) {
		
		ZProperty[] propertys = state.getPropertys();
		
		for (ZProperty prop : propertys) {
			
			if (prop instanceof ZPropertyDirection) {
				
				Direction d = state.getProperty((ZPropertyDirection) prop);
				d = d.rotate(direction);
				state.setProperty((ZPropertyDirection) prop, d);
				
			}
			
		}
		
	}
	
	public void step(World world, Vec2i pos, ZellState state) {
	}
	public void stepAkt(World world, Vec2i pos, ZellState state) {
	}
	
	public static void drawTexture(String texture, Vec2 offset) {
		drawTexture(texture, offset, 0);
	}
	
	public static void drawTexture(String texture) {
		drawTexture(texture, new Vec2(0, 0));
	}

	public static void drawTexture(String texture, int rotation) {
		drawTexture(texture, new Vec2(0, 0), rotation);
	}
	
	public static void drawTexture(String texture, Vec2 offset, int rotation) {
		float lx = offset.x;
		float ly = offset.y;
		TextureIO tex = Engine.getInstance().getResourceLoader().getTexture(texture);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(10, 10, 0);
		GL11.glRotatef(-rotation, 0, 0, 1);
		RenderHelper.renderTexture(lx - 10, ly - 10, 20, 20, tex);
		GL11.glPopMatrix();
		
	}
	
	public ZellState createState() {
		return new ZellState(this, new ZProperty[] {});
	}
	
	public ZellState getDefaultState() {
		return createState();
	}

	public abstract String getParticleTexture();
	
}
