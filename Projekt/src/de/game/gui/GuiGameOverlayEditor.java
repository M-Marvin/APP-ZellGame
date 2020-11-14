package de.game.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.game.Engine;
import de.game.gamelogic.GameRegistry;
import de.game.gamelogic.LevelDef;
import de.game.gamelogic.World;
import de.game.gamelogic.Zell;
import de.game.gamelogic.ZellState;
import de.game.gui.elements.GuiButton;
import de.game.gui.elements.GuiButtonTextured;
import de.game.gui.elements.GuiScrollBar;
import de.game.render.RenderHelper;
import de.game.util.InputHandler;
import de.game.util.JSONSerializer;
import de.game.util.Vec2;
import de.game.util.Vec2i;

public class GuiGameOverlayEditor extends GuiScreen {
	
	public GuiButtonTextured buttonStartStop;
	public GuiButtonTextured buttonStep;
	public GuiButtonTextured buttonReset;
	public GuiButtonTextured buttonSave;
	
	public GuiButtonTextured buttonDrawArea;
	public GuiButtonTextured buttonChangeEditeability;
	public GuiButtonTextured buttonPlaceParts;
	public int editMode = 3;
	
	public GuiButtonTextured buttonSaveAndExit;
	
	public ZellState heldZell;
	public Vec2i heldPos;
	public Vec2i pos1;
	public HashMap<Integer, ZellState> slots;
	public GuiScrollBar slotScrollbar;
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
		buttonSave = new GuiButtonTextured(290, 10, 60, 60, "gui/sim_save", GuiButton.COLOR_RED) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				resetState = null;
			}
		};
		buttonSave.setEnabled(false);
		guiElements.add(buttonSave);
		
		
		buttonDrawArea = new GuiButtonTextured(930, 500, 60, 60, "gui/draw_area", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				editMode = 1;
			}
		};
		guiElements.add(buttonDrawArea);
		buttonChangeEditeability = new GuiButtonTextured(930, 430, 60, 60, "gui/change_editeability", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				editMode = 2;
			}
		};
		guiElements.add(buttonChangeEditeability);
		buttonPlaceParts = new GuiButtonTextured(930, 360, 60, 60, "gui/place_parts", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				editMode = 3;
			}
		};
		guiElements.add(buttonPlaceParts);
		
		
		buttonSaveAndExit = new GuiButtonTextured(870, 10, 120, 60, "gui/save_level", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				LevelDef level = Engine.getInstance().saveLevel();
				JSONSerializer.saveLevelToFile(level);
				Engine.getInstance().stopLevel();
				Engine.getInstance().openGui(GuiOwnLevelSelect.class);
			}
		};
		guiElements.add(buttonSaveAndExit);
		
		this.slots = new HashMap<Integer, ZellState>();
		for (int i = 0; i < 6; i++) {
			this.slots.put(i, null);
		}
		
		slotScrollbar = new GuiScrollBar(80 + 2, 81, 480, new Color(0, 0, 0, 125), false);
		guiElements.add(slotScrollbar);
		
	}
	
	@Override
	public void renderBackground() {
		
		for (int pos : this.slots.keySet()) {
			
			RenderHelper.renderElementQuad(1, pos * 80 + 1 + 80, 80 - 2, 80 - 2, new Color(0, 0, 0, 125));
			
		}
		
		int x = 930;
		int y = editMode == 1 ? 500 : editMode == 2 ? 430 : 360;
		
		RenderHelper.renderElementQuad(x, y, 60, 60, GuiButton.COLOR_MOUSE_HOVER);
		
	}
	
	@Override
	public void renderForground() {
		
		for (Entry<Integer, ZellState> slot : this.slots.entrySet()) {
			
			ZellState state = slot.getValue();
			
			GL11.glPushMatrix();
			GL11.glTranslatef(10, slot.getKey() * 80 + 10 + 80, 0);
			GL11.glScalef(3, 3, 3);
			
			if (state != null) {
				Zell zell = state.getZell();
				zell.draw(Engine.getInstance().getWorld(), new Vec2i(0, 0), state);
			}
			GL11.glPopMatrix();
			
		}
		
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
	
	public boolean isOverSlot(float mx, float my) {
		return my > 80 && mx < 80;
	}
	
	public ZellState getStackInHoveredSlot(float mx, float my) {
		
		float exactSlotIndex = my - 80;
		int slotIndex = (int) (Math.min(5, exactSlotIndex / 80));
		ZellState stateInSlot = this.slots.get(slotIndex);
		return stateInSlot;
		
	}
	
	@Override
	public boolean updateTick() {
		
		buttonReset.setEnabled(resetState != null);
		buttonSave.setEnabled(resetState != null);
		buttonStartStop.setTexture(Engine.getInstance().isSimRunning() ? "gui/sim_stop" : "gui/sim_start");
		
		float scrollState = this.slotScrollbar.getScrollState();
		Zell[] zells = GameRegistry.getRegistredZells();
		int offset = (int) ((zells.length - 6) * scrollState);
		for (Entry<Integer, ZellState> slot : this.slots.entrySet()) {
			int index = slot.getKey() + offset;
			slot.setValue(zells[index].getDefaultState());
		}
		
		InputHandler handler = Engine.getInstance().getInputHandler();
		Vec2 coursor = handler.getWorldCoursorPos();
		float mx = handler.getMouseX();
		float my = handler.getMouseY();
		Vec2i zellPos = new Vec2i((int) coursor.x / 20, (int) coursor.y / 20);
		World world = Engine.getInstance().getWorld();
		
		if (!Engine.getInstance().isSimRunning()) {
			
			if (editMode == 3) {
				
				this.pos1 = null;
				
				if (handler.isMouseKeyTyped(2) && !isOverGui(mx, my)) {
					
					ZellState state = world.getZellState(zellPos);
					state.iteratePropertys();
					
				}
				
				if (Mouse.isButtonDown(0)) {
					
					if (this.heldZell == null) {

						if (isOverSlot(mx, my)) {
							
							ZellState state = getStackInHoveredSlot(mx, my);
							
							if (state != null) {
								
								this.heldZell = state.copy();
								
							}
							
						}
						
						ZellState state = world.getZellState(zellPos);
						if (!state.isEmpty() && !isOverGui(mx, my)) {
							this.heldZell = state;
							this.heldPos = zellPos.copy();
							world.removeZellState(zellPos);
						}
						
					}
					
				} else {
					
					if (this.heldZell != null) {
						
						if (isOverSlot(mx, my)) {
							this.heldZell = null;
						} else {
							
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
					
				}
				
			} else if (editMode == 2) {
				
				if (!isOverGui(mx, my)) {
					
					if (Mouse.isButtonDown(0)) {
						
						if (this.pos1 == null) this.pos1 = zellPos;
						
					} else if (this.pos1 != null) {
						
						Vec2i pos1 = this.pos1;
						Vec2i pos2 = zellPos;
						this.pos1 = null;
						
						for (int x = Math.min(pos1.x, pos2.x); x <= Math.max(pos1.x, pos2.x); x++) {
							for (int y = Math.min(pos1.y, pos2.y); y <= Math.max(pos1.y, pos2.y); y++) {
								
								Vec2i pos = new Vec2i(x, y);
								world.setEditeable(pos, !world.isEditeable(pos));
								
							}
						}
						
					}
				}
				
			} else if (editMode == 1) {
				
				if (!isOverGui(mx, my)) {
					
					if (Mouse.isButtonDown(0)) {
						
						if (this.pos1 == null) this.pos1 = zellPos;
						
					} else if (this.pos1 != null) {
						
						Vec2i pos1 = this.pos1;
						Vec2i pos2 = zellPos;
						this.pos1 = null;
						
						for (int x = Math.min(pos1.x, pos2.x); x <= Math.max(pos1.x, pos2.x); x++) {
							for (int y = Math.min(pos1.y, pos2.y); y <= Math.max(pos1.y, pos2.y); y++) {
								
								Vec2i pos = new Vec2i(x, y);
								world.setInWorld(pos, !world.isInWorld(pos));
								
							}
						}
						
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
