package com.countrygamer.cgo.library.common.lib

import java.util

import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier
import cpw.mods.fml.common.registry.{GameData, GameRegistry}
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

/**
 *
 *
 * @author CountryGamer
 */
object NameParser {

	def getName(itemStack: ItemStack, hasID: Boolean, hasMeta: Boolean): String = {
		if (itemStack == null) {
			return ""
		}

		val name: String =
			if (Block.getBlockFromItem(itemStack.getItem) == Blocks.air) {
				GameData.getItemRegistry.getNameForObject(itemStack.getItem)
			}
			else {
				GameData.getBlockRegistry.getNameForObject(
					Block.getBlockFromItem(itemStack.getItem)
				)
			}
		val ui: UniqueIdentifier = new UniqueIdentifier(name)
		(if (hasID) ui.modId + ":" else "") + name +
				(if (hasMeta) ":" + itemStack.getItemDamage else "")
	}

	def getItemStack(name: String): ItemStack = {
		var endNameIndex: Int = name.length
		var metadata: Int = OreDictionary.WILDCARD_VALUE
		// has meta
		if (name.matches("(.*):(.*):(.*)")) {
			endNameIndex = name.lastIndexOf(':')
			metadata = name.substring(endNameIndex + 1, name.length()).toInt
		}
		val modid: String = name.substring(0, name.indexOf(':'))
		val itemName: String = name.substring(name.indexOf(':') + 1, endNameIndex)
		val itemStack: ItemStack = GameRegistry.findItemStack(modid, itemName, 1)
		if (itemStack != null) {
			itemStack.setItemDamage(metadata)
		}
		itemStack
	}

	def isInCollection(itemStack: ItemStack, list: util.Collection[String]): Boolean = {
		list.contains(this.getName(itemStack, hasID = true, hasMeta = false)) ||
				list.contains(this.getName(itemStack, hasID = true, hasMeta = true))
	}

}
