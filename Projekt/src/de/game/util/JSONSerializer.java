package de.game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.game.gamelogic.LevelDef;
import de.game.gamelogic.ZellState;

public class JSONSerializer {
	
	public static void saveLevelToFile(LevelDef level) {
		
		JsonObject fileJson = serializeLevel(level);
		
		try {
			
			OutputStream os = new FileOutputStream(level.levelFile);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(fileJson, writer);
			writer.close();
			os.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Cant write Level to File!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static LevelDef loadLevelFromFile(File file) {
		
		try {
			
			InputStream is = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			Gson gson = new Gson();
			JsonObject fileJson = gson.fromJson(reader, JsonObject.class);
			reader.close();
			is.close();
			
			LevelDef level = deserializeLevel(fileJson);
			level.levelFile = file;
			
			return level;
			
		} catch (FileNotFoundException e) {
			System.err.println("ERROR cant load level!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static LevelDef deserializeLevel(JsonObject json) {
		
		LevelDef level = new LevelDef();
		
		JsonArray statesJson = json.get("ZellStates").getAsJsonArray();
		
		for (int i = 0; i < statesJson.size(); i++) {
			
			JsonObject stateJson = statesJson.get(i).getAsJsonObject();
			
			String stateS = stateJson.get("state").getAsString();
			JsonArray posJson = stateJson.get("position").getAsJsonArray();
			Vec2i position = deserializeVec2i(posJson);
			ZellState state = deserilizeZellState(stateS);
			
			level.zells.put(position, state);
			
		}
		
		JsonArray editZellsJson = json.get("EditeableZells").getAsJsonArray();
		
		for (int i = 0; i < editZellsJson.size(); i++) {
			
			level.editeableZells.add(deserializeVec2i(editZellsJson.get(i).getAsJsonArray()));
			
		}
		
		return level;
		
	}
	
	public static JsonObject serializeLevel(LevelDef level) {
		
		JsonObject json = new JsonObject();
		
		JsonArray statesJson = new JsonArray();
		for (Entry<Vec2i, ZellState> entry : level.zells.entrySet()) {
			
			JsonObject stateJson = new JsonObject();
			String state = serializeZellState(entry.getValue());
			stateJson.addProperty("state", state);
			JsonArray pos = serializeVec2i(entry.getKey());
			stateJson.add("position", pos);
			statesJson.add(stateJson);
			
		}
		
		json.add("ZellStates", statesJson);
		
		JsonArray editZellsJson = new JsonArray();
		
		for (Vec2i pos : level.editeableZells) {
			
			editZellsJson.add(serializeVec2i(pos));
			
		}
		
		json.add("EditeableZells", editZellsJson);
		
		return json;
		
	}
	
	public static ZellState deserilizeZellState(String s) {
		return ZellState.fromString(s);
	}
	
	public static String serializeZellState(ZellState state) {
		return state.toString();
	}
	
	public static JsonArray serializeVec2(Vec2 vec2) {
		JsonArray json = new JsonArray();
		json.add(vec2.x);
		json.add(vec2.y);
		return json;
	}

	public static Vec2 deserializeVec2(JsonArray json) {
		if (json.size() == 2) {
			float x = json.get(0).getAsFloat();
			float y = json.get(1).getAsFloat();
			return new Vec2(x, y);
		}
		return new Vec2(0, 0);
	}
	
	public static JsonArray serializeVec2i(Vec2i vec2) {
		JsonArray json = new JsonArray();
		json.add(vec2.x);
		json.add(vec2.y);
		return json;
	}

	public static Vec2i deserializeVec2i(JsonArray json) {
		if (json.size() == 2) {
			int x = json.get(0).getAsInt();
			int y = json.get(1).getAsInt();
			return new Vec2i(x, y);
		}
		return new Vec2i(0, 0);
	}
	
}
