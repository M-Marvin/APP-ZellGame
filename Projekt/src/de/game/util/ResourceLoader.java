package de.game.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.game.Engine;
import de.game.render.TextureIO;

public class ResourceLoader {
	
	private File resourceFolder;
	
	public ResourceLoader(File resourceFolder) {
		this.resourceFolder = resourceFolder;
	}
	
	public File getResourceFolder() {
		return resourceFolder;
	}
	
	public File getResource(ResourceType type, String path) {
		return new File(resourceFolder, "/" + type.getFolder() + "/" + path + type.getFileType());
	}
	
	public File getResourceFolder(ResourceType type) {
		return new File(resourceFolder, "/" + type.getFolder() + "/");
	}
	
	protected HashMap<String, TextureIO> loadedTextures = new HashMap<String, TextureIO>();
	public TextureIO getTexture(String texture) {
		
		TextureIO tex = loadedTextures.get(texture);
		
		if (tex == null) {

			File textureFile = getResource(ResourceType.TEXTURE, texture);
			
			try {
				tex = new TextureIO(new FileInputStream(textureFile));
			} catch (FileNotFoundException e) {
				System.out.println("Texture " + texture + " cant loaded.");
				e.printStackTrace();
				tex = TextureIO.DEFAULT_TEXTURE;
			} catch (IOException e) {
				e.printStackTrace();
				tex = TextureIO.DEFAULT_TEXTURE;
			}
			
			loadedTextures.put(texture, tex);
			
		}
		
		return tex;
		
	}

	public File getSound(String res) {
		
		File iniFile = getResource(ResourceType.SOUND_INI, res);
		
		if (iniFile.exists()) {
			
			try {
				
				int variants = 0;
				
				InputStream stream = new FileInputStream(iniFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				Gson gson = new Gson();
				
				JsonObject fileJson = gson.fromJson(reader, JsonObject.class);
				
				if (fileJson.has("variants")) {
					
					variants = fileJson.get("variants").getAsInt();
					
				}
				
				reader.close();
				stream.close();
				
				int variant = Engine.getInstance().getWorld().getRandom().nextInt(variants - 1);
				
				String soundName = res + "_" + variant;
				
				return getResource(ResourceType.SOUND_FILE, soundName);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		return getResource(ResourceType.SOUND_FILE, res);
		
	}
	
	public static enum ResourceType {

		SOUND_INI("sounds", ".ini"),SOUND_FILE("sounds", ".wav"),TEXTURE("textures", ".png"),SHADER("shader", ""),LEVEL("levels", ".json");
		
		private String folder;
		private String fileType;
		
		ResourceType(String folder, String fileType) {
			this.folder = folder;
			this.fileType = fileType;
		}
		
		public String getFolder() {
			return folder;
		}
		
		public String getFileType() {
			return fileType;
		}
		
	}
	
}
