package com.countrygamer.countrygamercore.lib;

public class UtilVector {
	
	public static int getQuad(double offset_X, double offset_Z) {
		if (offset_X > 0 && offset_Z >= 0)
			return 1;
		else if (offset_X <= 0 && offset_Z > 0)
			return 2;
		else if (offset_X < 0 && offset_Z <= 0)
			return 3;
		else if (offset_X >= 0 && offset_Z < 0)
			return 4;
		else
			return 0;
	}
	
	public static double getRotationFromCoords(double pivotX, double pivotZ, double targetX,
			double targetZ) {
		double offset_X = -(pivotX - targetX);
		double offset_Z = -(pivotZ - targetZ);
		
		double rotaion_radians = Math.atan(offset_Z / offset_X);
		double rotation_deg = rotaion_radians * 180 / Math.PI;
		
		int quad = getQuad(offset_X, offset_Z);
		if (quad == 3 || quad == 4) {
			rotation_deg += 180;
		}
		
		return rotation_deg;
	}
	
}
