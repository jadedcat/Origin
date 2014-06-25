package com.countrygamer.countrygamercore.common.lib;

public enum Activity {
	PULSE(new String[] {
		"Active once per redstone pulse (while settings apply)"
	}), WHILE(new String[] {
		"Active while redstone pulse (while settings apply)"
	});
	
	public final String[] desc;
	
	private Activity(String[] desc) {
		this.desc = desc;
	}
	
	public static Activity getState(int stateID) {
		if (stateID < 0)
			return WHILE;
		else if (stateID == 0)
			return PULSE;
		else if (stateID == 1)
			return WHILE;
		else if (stateID > 1)
			return PULSE;
		else
			return null;
	}
	
	public static int getInt(Activity state) {
		if (state == PULSE)
			return 0;
		else if (state == WHILE)
			return 1;
		else
			return -1;
	}
	
}
