package com.countrygamer.countrygamercore.base.client.gui.advanced;

public enum RenderRotation {
	NORMAL, ROTATE_90, ROTATE_180, ROTATE_270,
	
	FLIP_HORIZONTAL, ROTATE_90_FLIP, FLIP_VERTICAL, ROTATE_270_FLIP;
	
	public RenderRotation getNextRotation() {
		switch (ordinal()) {
			case 1:
			default:
				return ROTATE_90;
			case 2:
				return ROTATE_180;
			case 3:
				return ROTATE_270;
			case 4:
				return NORMAL;
			case 5:
				return ROTATE_90_FLIP;
			case 6:
				return FLIP_VERTICAL;
			case 7:
				return ROTATE_270_FLIP;
			case 8:
		}return FLIP_HORIZONTAL;
	}
	
	public RenderRotation getFlippedRotation()
	{
		switch (ordinal()) {
			case 1:
			default:
				return FLIP_HORIZONTAL;
			case 2:
				return ROTATE_90_FLIP;
			case 3:
				return FLIP_VERTICAL;
			case 4:
				return ROTATE_270_FLIP;
			case 5:
				return NORMAL;
			case 6:
				return ROTATE_90;
			case 7:
				return ROTATE_180;
			case 8:
		}return ROTATE_270;
	}
}
