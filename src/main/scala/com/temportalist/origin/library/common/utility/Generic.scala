package com.temportalist.origin.library.common.utility

import java.util.List

/**
 *
 *
 * @author TheTemportalist
 */
object Generic {

	def getR(color: Int): Int = ((0xff000000 | color) >> 16) & 0xFF

	def getG(color: Int): Int = ((0xff000000 | color) >> 8) & 0xFF

	def getB(color: Int): Int = ((0xff000000 | color) >> 0) & 0xFF

	def addToList[T](list: List[_], obj: T): Unit = {
		list.asInstanceOf[List[T]].add(obj)
	}

}
