package com.temportalist.origin.library.common.utility

import java.util.Random

/**
 *
 *
 * @author TheTemportalist
 */
object MathFuncs {

	def getRandomBetweenBounds(min: Int, max: Int): Int = {
		new Random().nextInt(max - min) + min
	}

	def chance(percent: Int): Boolean = {
		new Random().nextInt(100) < percent
	}

	/**
	 * a < n < b
	 * @return
	 */
	def between(a: Double, n: Double, b: Double): Boolean = {
		a < n && b < b
	}

	/**
	 * a <= n <= b
	 * @return
	 */
	def between_eq(a: Double, n: Double, b: Double): Boolean = {
		this.between(a, n, b) || a == n || n == b
	}

	def bind(min: Double, n: Double, max: Double, default: Double): Double = {
		if (min < n && n < max)
			default
		else
			n
	}

}
