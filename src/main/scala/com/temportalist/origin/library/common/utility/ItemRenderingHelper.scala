package com.temportalist.origin.library.common.utility

import java.util

import com.temportalist.origin.wrapper.common.rendering.IRenderingObject

/**
 *
 *
 * @author TheTemportalist
 */
object ItemRenderingHelper {

	val registeredItems: util.List[IRenderingObject] = new util.ArrayList[IRenderingObject]()

	def register(item: IRenderingObject): Unit = this.registeredItems.add(item)

	def registerItemRenders(): Unit =
		for (i <- 0 until this.registeredItems.size()) {
			this.registeredItems.get(i).initRendering()
		}

}
