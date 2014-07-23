package com.countrygamer.cgo.common.lib

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
		return null
	}

	def getFromNBT(compound: NBTTagCompound): ItemMeta = {
		val im: ItemMeta = new ItemMeta(null.asInstanceOf[Item], 0)
		im.loadFromNBT(compound)
		return im
	}

}
