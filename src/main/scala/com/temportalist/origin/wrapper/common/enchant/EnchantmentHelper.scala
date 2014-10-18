package com.temportalist.origin.wrapper.common.enchant

import scala.util.control.Breaks._

import net.minecraft.enchantment.Enchantment

/**
 *
 *
 * @author CountryGamer
 */
object EnchantmentHelper {

	final def getNewID: Int = {
		var id: Int = -1
		var it: Int = 0
		breakable {
			for (it <- 0 to Enchantment.enchantmentsList.length) {
				if (Enchantment.enchantmentsList(it) == null) {
					id = it
					break()
				}
			}
		}
		if (id < 0) {
			id = Enchantment.enchantmentsList.length
		}
		id
	}

}
