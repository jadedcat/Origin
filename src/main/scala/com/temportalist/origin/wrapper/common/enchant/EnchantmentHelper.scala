package com.temportalist.origin.wrapper.common.enchant

import scala.util.control.Breaks._

import net.minecraft.enchantment.Enchantment

/**
 *
 *
 * @author TheTemportalist
 */
object EnchantmentHelper {

	final def getNewID: Int = {
		var id: Int = -1
		breakable {
			for (it <- 0 to Enchantment.enchantmentsBookList.length) {
				if (Enchantment.enchantmentsBookList(it) == null) {
					id = it
					break()
				}
			}
		}
		if (id < 0) {
			id = Enchantment.enchantmentsBookList.length
		}
		id
	}

}
