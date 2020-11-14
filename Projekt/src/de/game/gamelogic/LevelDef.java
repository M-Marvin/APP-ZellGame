package de.game.gamelogic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.game.util.Vec2i;

public class LevelDef {
	
	public File levelFile;
	public HashMap<Vec2i, ZellState> zells;
	public List<Vec2i> editeableZells;
	
	public LevelDef() {
		zells = new HashMap<Vec2i, ZellState>();
		editeableZells = new ArrayList<Vec2i>();
	}
	
}
