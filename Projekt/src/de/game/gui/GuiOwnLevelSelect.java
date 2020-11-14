package de.game.gui;

import java.io.File;

import de.game.Engine;
import de.game.gamelogic.LevelDef;
import de.game.gui.elements.GuiButton;
import de.game.gui.elements.GuiButtonTextured;
import de.game.gui.elements.GuiList;
import de.game.gui.elements.List_StringItem;
import de.game.util.JSONSerializer;
import de.game.util.ResourceLoader.ResourceType;

public class GuiOwnLevelSelect extends GuiScreen {
	
	public GuiList<List_StringItem> levelList;
	public GuiButtonTextured buttonPlayLevel;
	public GuiButtonTextured buttonCopyLevel;
	public GuiButtonTextured buttonEditLevel;
	public GuiButtonTextured buttonNewLevel;
	public GuiButtonTextured buttonDeleteLevel;
	
	@Override
	public boolean isOverGui(float x, float y) {
		return true;
	}

	@Override
	public void onPressEscape() {
		// TODO
	}

	@Override
	public void init(Object... args) {
		
		levelList = new GuiList<List_StringItem>(40, 80, 920, 510, GuiButton.COLOR_NORMAL);
		guiElements.add(levelList);
		
		buttonPlayLevel = new GuiButtonTextured(40, 10, 120, 60, "gui/play_level", GuiButton.COLOR_GREEN) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				LevelDef level = getSelectedLevel();
				if (level != null) Engine.getInstance().startLevel(level, false);
			}
		};
		guiElements.add(buttonPlayLevel);
		buttonEditLevel = new GuiButtonTextured(170, 10, 120, 60, "gui/edit_level", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				LevelDef level = getSelectedLevel();
				if (level != null) Engine.getInstance().startLevel(level, true);
			}
		};
		guiElements.add(buttonEditLevel);
		buttonCopyLevel = new GuiButtonTextured(300, 10, 120, 60, "gui/copy_level", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				// TODO Auto-generated method stub
				
			}
		};
		guiElements.add(buttonCopyLevel);
		buttonNewLevel = new GuiButtonTextured(430, 10, 120, 60, "gui/new_level", GuiButton.COLOR_NORMAL) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				// TODO Auto-generated method stub
				
			}
		};
		guiElements.add(buttonNewLevel);
		buttonDeleteLevel = new GuiButtonTextured(560, 10, 120, 60, "gui/delete_level", GuiButton.COLOR_RED) {
			@Override
			public void onClicked(float mouseX, float mouseZ) {
				// TODO Auto-generated method stub
				
			}
		};
		guiElements.add(buttonDeleteLevel);
		
		File levelFolder = Engine.getInstance().getResourceLoader().getResourceFolder(ResourceType.LEVEL);
		String[] levels = levelFolder.list();
		for (String level : levels) {
			this.levelList.addItemToList(new List_StringItem(level.split("\\.")[0]));
		}
		
	}
	
	public LevelDef getSelectedLevel() {
		
		if (this.levelList.getSelectedItem() != null) {
			String levelName = this.levelList.getSelectedItem().getString();
			File levelFile = Engine.getInstance().getResourceLoader().getResource(ResourceType.LEVEL, levelName);
			LevelDef level = JSONSerializer.loadLevelFromFile(levelFile);
			return level;
		}
		
		return null;
		
	}
	
	@Override
	public void renderBackground() {
		// TODO Auto-generated method stub
		super.renderBackground();
	}
	
}
