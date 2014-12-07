package com.temportalist.origin.library.common.lib

/**
 *
 *
 * @author TheTemportalist
 */
object Generic {

	def getR(color: Int): Int = ((0xff000000 | color) >> 16) & 0xFF

	def getG(color: Int): Int = ((0xff000000 | color) >> 8) & 0xFF

	def getB(color: Int): Int = ((0xff000000 | color) >> 0) & 0xFF

}
