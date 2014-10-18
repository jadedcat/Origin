package com.temportalist.origin.library.common.utility

import java.util.Random

/**
 *
 *
 * @author CountryGamer
 */
object MathFuncs {

	def getRandomBetweenBounds(min: Int, max: Int): Int = {
		new Random().nextInt(max - min) + min
	}

	def chance(percent: Int): Boolean = {
		new Random().nextInt(100) < percent
	}

}
