package de.game;

import org.lwjgl.input.Keyboard;

import de.game.gamelogic.GameRegistry;
import de.game.gamelogic.Zell;
import de.game.gamelogic.zellpropertys.ZPropertyDirection;
import de.game.gamelogic.zellpropertys.ZPropertyRotDirection;
import de.game.objects.DuplicatorZell;
import de.game.objects.ExplosivZell;
import de.game.objects.PusheableZell;
import de.game.objects.PusherZell;
import de.game.objects.RotatingZell;
import de.game.objects.SlidingZell;
import de.game.objects.WallZell;

public class Game {
	
	public static final Zell pusher_zell = new PusherZell();
	public static final Zell pusheable_zell = new PusheableZell();
	public static final Zell duplicator_zell = new DuplicatorZell();
	public static final Zell sliding_zell = new SlidingZell();
	public static final Zell rotating_zell = new RotatingZell();
	public static final Zell explosiv_zell = new ExplosivZell();
	public static final Zell wall_zell = new WallZell();
	
	public static void init() {
		
		Engine.getInstance().getInputHandler().registerKeybinging("control.escape", Keyboard.KEY_ESCAPE);
		
		// Zells
		GameRegistry.registerZell(pusher_zell, "pusher_zell");
		GameRegistry.registerZell(pusheable_zell, "pusheable_zell");
		GameRegistry.registerZell(duplicator_zell, "duplicator_zell");
		GameRegistry.registerZell(sliding_zell, "sliding_zell");
		GameRegistry.registerZell(rotating_zell, "rotating_zell");
		GameRegistry.registerZell(explosiv_zell, "explosiv_zell");
		GameRegistry.registerZell(wall_zell, "wall_zell");
		
		// ZPRopertys
		GameRegistry.registerProp(ZPropertyDirection.class, "Direction");
		GameRegistry.registerProp(ZPropertyRotDirection.class, "RotDirection");
		
	}

}
