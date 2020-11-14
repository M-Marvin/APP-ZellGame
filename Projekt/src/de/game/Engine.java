package de.game;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Field;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.game.gamelogic.GameRegistry;
import de.game.gamelogic.LevelDef;
import de.game.gamelogic.World;
import de.game.gui.GuiGameOverlay;
import de.game.gui.GuiGameOverlayEditor;
import de.game.gui.GuiOwnLevelSelect;
import de.game.gui.GuiScreen;
import de.game.render.GLStateManager;
import de.game.render.Shader;
import de.game.util.InputHandler;
import de.game.util.ResourceLoader;
import de.game.util.SoundPlayer;

public class Engine {
	
	public static final DisplayMode START_DISPLAY_MODE = new DisplayMode(1000, 600);
	public static final Color INITIAL_COLOR = new Color(100, 100, 100);
	public static final int viewSizeX = 1000;
	public static final int viewSizeY = 600;
	public static final float MAX_WORLD_ZOOM = 6.0F;
	public static final float MIN_WORLD_ZOOM = 0.5F;
	public static final float WORLD_ZOOM_SPEED = 1F;
	public static final float DEFAULT_WORLD_ZOOM = 3F;
	
	private static Engine instance;
	
	private File systemPath;
	private ResourceLoader resourceLoader;
	private SoundPlayer soundPlayer;
	private InputHandler inputHandler;
	private GameRegistry gameRegistry;
	private boolean devMode;
	
	private int FPScount;
	private long lastFPSCheck;
	public int FPS;
	
	public de.game.util.Vec2 worldTranslate;
	public float worldZoom;
	public boolean isCloseRequested;
	
	public World world;
	public GuiScreen openGui;
	public LevelDef currentLevel;
	
	private Shader usedShader;
	
	public static Engine getInstance() {
		return instance;
	}
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public void start() {

		System.out.println("Start!");
		instance = this;
		
		/** Initialisate Game-Variables */
		
		systemPath = new File(ClassLoader.getSystemResource("").getPath().substring(1).replace("%20", " "));
		File resourceFolder = new File(systemPath, "res/");
		devMode = !resourceFolder.exists() || !resourceFolder.isDirectory();
		if (devMode) resourceFolder = new File(systemPath.getParentFile(), "res/");
		File libpath = new File(resourceFolder, "native/");
		
		try {
			System.out.println("LibraryPath: " + libpath);
			System.out.println("SystemPath: "+ systemPath);
			System.setProperty("java.library.path", libpath.getAbsolutePath());
			Field field = ClassLoader.class.getDeclaredField("sys_paths");
			field.setAccessible(true);
			field.set(null, null);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e) {
			System.err.println("ERROR SYSTEM EXCEPTION");
			e.printStackTrace();
			System.exit(-1);
		}
		
		resourceLoader = new ResourceLoader(resourceFolder);
		soundPlayer = new SoundPlayer(resourceLoader);
		inputHandler = new InputHandler();
		gameRegistry = new GameRegistry();
		
		worldTranslate = new de.game.util.Vec2(0, 0);
		worldZoom = DEFAULT_WORLD_ZOOM;
		
		/** Create Display */
		
		try {
			
			Display.setDisplayMode(START_DISPLAY_MODE);
			Display.setResizable(true);
			Display.setInitialBackground(INITIAL_COLOR.getRed() / 255F, INITIAL_COLOR.getGreen() / 255F, INITIAL_COLOR.getBlue() / 255F);
			Display.setTitle("BlockGame");
			Display.create();
			
		} catch (LWJGLException e) {
			System.err.println("ERRROR: Can not create Display!");
			System.exit(-1);
		}
		
		/** Initialisate OpenGL */
		
		GLStateManager.reconfigurateOrtho(viewSizeX, viewSizeY);
		
		usedShader = new Shader();
		usedShader.create("basic");
		
		/** Start Game-Loop */
		Game.init();
		
		openGui(GuiOwnLevelSelect.class);
		
		while (!isCloseRequested && !Display.isCloseRequested()) {
			
			if (Display.wasResized()) GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			
			gameTick();
			
			FPScount++;
			if (getTime() - lastFPSCheck > 1000) {
				lastFPSCheck = getTime();
				FPS = FPScount;
				FPScount = 0;
			}
			
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
			GL11.glLoadIdentity();
			
			this.usedShader.useShader();
			renderTick();
			
			Display.sync(60);
			Display.update();
			
			Display.setTitle("FPS: " + this.FPS);
			
		}
		
		usedShader.destroy();
		
		Display.destroy();
		System.out.println("End with Exitcode 0");
		System.exit(0);
		
	}

	public static void stop() {
		Engine.getInstance().isCloseRequested = true;
	}
	
	public DisplayMode getGraficalSize() {
		return Display.getDisplayMode();
	}
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	
	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}
	
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	public GameRegistry getGameRegistry() {
		return gameRegistry;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void closeGui() {
		this.openGui.onGuiClosed();
		this.openGui = null;
	}
	
	public GuiScreen getOpenGui() {
		return openGui;
	}
	
	public void openGui(Class<? extends GuiScreen> gui, Object... args) {
		
		if (this.openGui != null) closeGui();
		
		try {
			this.openGui = gui.newInstance();
			this.openGui.init(args);
		} catch (InstantiationException e) {
			System.out.println("Error cant open Gui!");
			this.closeGui();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Error cant open Gui!");
			this.closeGui();
			e.printStackTrace();
		}
		
	}
	
	public static void closeWorld() {
		Engine.getInstance().world = null;
	}
	
	public static void openWorld(World world) {
		Engine.getInstance().world = world;
	}
	
	private int stepMode = 0;
	public void startSim() {
		stepMode = 1;
	}
	
	public void stopSim() {
		stepMode = 0;
	}
	
	public void stepSim() {
		stepMode = 2;
	}
	
	public void setStepMode(int mode) {
		stepMode = mode;
	}
	
	public int getStepMode() {
		return stepMode;
	}
	
	public boolean isSimRunning() {
		return stepMode > 0;
	}
	
	public void loadSim(LevelDef level) {
		world = new World(level);
		stepMode = 0;
	}
	
	public void startLevel(LevelDef level, boolean editorMode) {
		this.currentLevel = level;
		loadSim(level);
		this.openGui(editorMode ? GuiGameOverlayEditor.class : GuiGameOverlay.class);
	}
	
	public void stopLevel() {
		this.currentLevel = null;
		this.world = null;
	}
	
	public LevelDef saveLevel() {
		
		LevelDef newLevel = this.world.getLevelDef();
		newLevel.levelFile = this.currentLevel.levelFile;
		this.currentLevel = newLevel;
		return this.currentLevel;
		
	}
	
	public void gameTick() {
		
		inputHandler.update();
		
		if (this.world != null) {
			
			float scroll = inputHandler.getMouseDW();
			if ((this.worldZoom > MIN_WORLD_ZOOM && scroll < 0) || (this.worldZoom < MAX_WORLD_ZOOM && scroll > 0)) {
				float tmp = this.worldZoom;
				this.worldZoom += inputHandler.getWorldZoom(this.worldZoom);
				this.worldZoom = Math.min(MAX_WORLD_ZOOM, Math.max(MIN_WORLD_ZOOM, this.worldZoom));
				tmp = (this.worldZoom - tmp) / tmp;
				this.worldTranslate.x += tmp * (this.worldTranslate.x - viewSizeX / 2);
				this.worldTranslate.y += tmp * (this.worldTranslate.y);
				
			}
			
			if (Mouse.isButtonDown(1)) {
				this.worldTranslate.x += inputHandler.getMouseDX();
				this.worldTranslate.y += inputHandler.getMouseDY();
			}
			
			this.world.tick(stepMode > 0);
			if (stepMode == 2) stepMode = 0; 
			
		}
		
		if (this.openGui != null) {
			
			this.openGui.updateTick();
			
		}
		
	}
	
	public void renderTick() {
		
		if (world != null) {

			GL11.glPushMatrix();
			
			GL11.glTranslatef(worldTranslate.x, worldTranslate.y, 0);
			float s = worldZoom;
			GL11.glScalef(s, s, s);
			
			world.draw();
			
			GL11.glPopMatrix();
			
		}
		
		if (this.openGui != null) {
			
			GL11.glPushMatrix();
			
			this.openGui.renderBackground();
			this.openGui.renderForground();

			GL11.glPopMatrix();
			
		}
		
	}
	
}
