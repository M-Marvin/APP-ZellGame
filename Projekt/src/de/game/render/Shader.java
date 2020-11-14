package de.game.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import de.game.Engine;
import de.game.util.ResourceLoader.ResourceType;

public class Shader {
	
	protected int vertexShader;
	protected int fragmentShader;
	protected int geometryShader;
	protected int program;
	
	public boolean create(String shader) {
		
		int success;
		
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShader, readSource(shader + ".vs"));
		GL20.glCompileShader(vertexShader);
		
		success = GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE) {
			System.err.println("ERROR on compile vertexShader");
			System.err.println(GL20.glGetShaderInfoLog(vertexShader, 100));
			return false;
		}
		
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShader, readSource(shader + ".fs"));
		GL20.glCompileShader(fragmentShader);
		
		success = GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE) {
			System.err.println("ERROR on compile fragmentShader");
			System.err.println(GL20.glGetShaderInfoLog(fragmentShader, 100));
			return false;
		}
		
		geometryShader = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
		GL20.glShaderSource(geometryShader, readSource(shader + ".gs"));
		GL20.glCompileShader(geometryShader);
		
		success = GL20.glGetShaderi(geometryShader, GL20.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE) {
			System.err.println("ERROR on compile geometryShader");
			System.err.println(GL20.glGetShaderInfoLog(geometryShader, 100));
			return false;
		}
		
		program = GL20.glCreateProgram();
		
		GL20.glAttachShader(program, vertexShader);
		GL20.glAttachShader(program, fragmentShader);
		//GL20.glAttachShader(program, geometryShader);
		
		GL20.glLinkProgram(program);
		
		success = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
		if (success == GL11.GL_FALSE) {
			System.err.println("ERROR on link program");
			System.err.println(GL20.glGetProgramInfoLog(program, 100));
			return false;
		}
		
		GL20.glValidateProgram(program);
		
		success = GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS);
		if (success == GL11.GL_FALSE) {
			System.err.println("ERROR on validate program");
			System.err.println(GL20.glGetProgramInfoLog(program, 100));
			return false;
		}
		
		return true;
		
	}
	
	public void destroy() {
		
		GL20.glDetachShader(program, vertexShader);
		GL20.glDetachShader(program, fragmentShader);
		GL20.glDeleteShader(vertexShader);
		GL20.glDeleteShader(fragmentShader);
		GL20.glDeleteProgram(program);
		
	}
	
	public void useShader() {
		GL20.glUseProgram(program);
	}
	
	public String readSource(String file) {
		
		File path = Engine.getInstance().getResourceLoader().getResource(ResourceType.SHADER, file);
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String line;
			StringBuilder sourceBuilder = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sourceBuilder.append(line + "\n");
			}
			
			reader.close();
			
			return sourceBuilder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
