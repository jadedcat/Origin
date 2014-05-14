package com.countrygamer.countrygamercore.lib;

public enum RedstoneState {
	IGNORE(new String[] {
		"Ignore any nearby redstone signals"
	}), LOW(new String[] {
		"Only active without a redstone signal"
	}), HIGH(new String[] {
		"Only active with a redstone signal"
	});
	
	public final String[] desc;
	
	private RedstoneState(String[] desc) {
		this.desc = desc;
	}

	public static RedstoneState getStateFromInt(int stateID) {
		switch (stateID) {
			case 0:
				return RedstoneState.IGNORE;
			case 1:
				return RedstoneState.LOW;
			case 2:
				return RedstoneState.HIGH;
			default:
				return null;
		}
	}
	
	public static int getIntFromState(RedstoneState state) {
		if (state == RedstoneState.IGNORE)
			return 0;
		else if (state == RedstoneState.LOW)
			return 1;
		else if (state == RedstoneState.HIGH)
			return 2;
		else
			return -1;
	}
	
}
