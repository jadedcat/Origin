package com.temportalist.origin.library.common.utility

import java.util

import com.temportalist.origin.wrapper.common.item.ItemWrapper
/**
 *
 *
 * @author TheTemportalist
 */
object ItemRenderingHelper {

	val registeredItems: util.List[ItemWrapper] = new util.ArrayList[ItemWrapper]()

	def registerItemForRender(item: ItemWrapper): Unit = this.registeredItems.add(item)

	def registerItemRenders(): Unit =
		for (i <- 0 until this.registeredItems.size()) {
			this.registeredItems.get(i).initRender()
		}

}
