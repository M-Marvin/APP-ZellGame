package de.game.util;

import java.util.ArrayList;
import java.util.List;

public enum RotDirection {
	
	CW,CCW;

	public static List<RotDirection> valuesList() {
		List<RotDirection> values = new ArrayList<RotDirection>();
		values.add(CW);
		values.add(CCW);
		return values;
	}

	public static RotDirection fromString(String s) {
		switch (s) {
		case "CW": return CW;
		case "CCW": return CCW;
		default: return CW;
		}
	}
	
}
