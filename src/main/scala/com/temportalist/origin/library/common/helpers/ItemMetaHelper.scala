package com.temportalist.origin.library.common.helpers

import com.temportalist.origin.library.common.lib.ItemMeta
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author CountryGamer
 */
object ItemMetaHelper {

	def getFromStack(itemStack: ItemStack): ItemMeta = {
		if (itemStack != null) {
			return new ItemMeta(itemStack.getItem, itemStack.getItemDamage)
		}
		null
	}

	def getFromNBT(compound: NBTTagCompound): ItemMeta = {
		val im: ItemMeta = new ItemMeta(null.asInstanceOf[Item], 0)
		im.loadFromNBT(compound)
		im
	}

}
