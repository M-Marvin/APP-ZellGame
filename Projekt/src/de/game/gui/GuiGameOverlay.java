package de.game.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.game.Engine;
import de.game.gamelogic.LevelDef;
import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gui.elements.GuiButton;
import de.game.gui.elements.GuiButtonTextured;
import de.game.util.InputHandler;
import de.game.util.Vec2;
import de.game.util.Vec2i;

public class GuiGameOverlay extends GuiScreen {
	
	public GuiButtonTextured buttonStartStop;
	public GuiButtonTextured buttonStep;
	public GuiButtonTextured buttonReset;
	
	public GuiButtonTextured buttonExit;
	
	public ZellState heldZell;
	public Vec2i heldPos;
	public LevelDef resetState;
	
	@Override
	public boolean isOverGui(float x, float y) {
		return y < 80 || x < 100 || x > 920;
	}

	@Override
	public void onPressEscape() {}
	
	@Override
	public void init(Object... args) {
		
		buttonStartStop = new GuiButtonTextured(10, 10, 60, 60, "gui/sim_stop", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				if (Engine.getInstance().isSimRunning()) {
					Engine.getInstance().stopSim();
				} else {
					if (resetState == null) resetState = Engine.getInstance().getWorld().getLevelDef();
					Engine.getInstance().startSim();
				}
			}
		};
		guiElements.add(buttonStartStop);
		buttonStep = new GuiButtonTextured(80, 10, 60, 60, "gui/sim_step", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				if (resetState == null) resetState = Engine.getInstance().getWorld().getLevelDef();
				Engine.getInstance().stepSim();
			}
		};
		guiElements.add(buttonStep);
		buttonReset = new GuiButtonTextured(220, 10, 60, 60, "gui/sim_reset", GuiButton.COLOR_RED) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				if (resetState != null) Engine.getInstance().loadSim(resetState);
				resetState = null;
			}
		};
		buttonReset.setEnabled(false);
		guiElements.add(buttonReset);
		
		
		buttonExit = new GuiButtonTextured(870, 10, 120, 60, "gui/exit_level", GuiButton.COLOR_RED) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				Engine.getInstance().stopLevel();
				Engine.getInstance().openGui(GuiOwnLevelSelect.class);
			}
		};
		guiElements.add(buttonExit);
		
	}
	
	@Override
	public void renderBackground() {}
	
	@Override
	public void renderForground() {
		
		super.renderForground();

		if (heldZell != null) {
			
			InputHandler handler = Engine.getInstance().getInputHandler();
			float mx = handler.getMouseX();
			float my = handler.getMouseY();
			float s = Engine.getInstance().worldZoom;
			
			GL11.glPushMatrix();
			GL11.glTranslatef(mx, my, 0);
			GL11.glScalef(s, s, s);
			GL11.glTranslatef(-10, -10, 0);
			
			Zell zell = this.heldZell.getZell();
			zell.draw(Engine.getInstance().getWorld(), new Vec2i(0, 0), this.heldZell);
			GL11.glPopMatrix();
			
		}
		
	}
	
	@Override
	public boolean updateTick() {
		
		buttonReset.setEnabled(resetState != null);
		buttonStartStop.setTexture(Engine.getInstance().isSimRunning() ? "gui/sim_stop" : "gui/sim_start");
				
		InputHandler handler = Engine.getInstance().getInputHandler();
		Vec2 coursor = handler.getWorldCoursorPos();
		float mx = handler.getMouseX();
		float my = handler.getMouseY();
		Vec2i zellPos = new Vec2i((int) coursor.x / 20, (int) coursor.y / 20);
		World world = Engine.getInstance().getWorld();
		
		if (!Engine.getInstance().isSimRunning() && resetState == null) {
			
			if (handler.isMouseKeyTyped(2) && !isOverGui(mx, my)) {
				
				ZellState state = world.getZellState(zellPos);
				state.iteratePropertys();
				
			}
			
			if (Mouse.isButtonDown(0)) {
				
				if (this.heldZell == null) {
										
					ZellState state = world.getZellState(zellPos);
					if (!state.isEmpty() && !isOverGui(mx, my)) {
						this.heldZell = state;
						this.heldPos = zellPos.copy();
						world.removeZellState(zellPos);
					}
					
				}
				
			} else {
				
				if (this.heldZell != null) {
					
					ZellState state = world.getZellState(zellPos);
					if (state.isEmpty()) {
						world.setZellState(zellPos, heldZell);
						this.heldZell = null;
					} else {
						ZellState tmp = world.getZellState(zellPos);
						world.removeZellState(zellPos);
						world.setZellState(zellPos, heldZell);
						world.setZellState(heldPos, tmp);
						this.heldZell = null;
					}
					
				}
				
			}
			
		} else if (this.heldZell != null) {
			world.setZellState(heldPos, heldZell);
			this.heldZell = null;
		}
		
		if (this.heldZell == null) return super.updateTick();
		return false;
		
	}
	
}
