package com.temportalist.origin.library.common.lib

import java.util

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier
import net.minecraftforge.fml.common.registry.{GameData, GameRegistry}
import net.minecraftforge.oredict.OreDictionary

/**
 *
 *
 * @author TheTemportalist
 */
object NameParser {

	def getName(itemStack: ItemStack, hasID: Boolean, hasMeta: Boolean): String = {
		if (itemStack == null) {
			return ""
		}

		val name: String =
			if (Block.getBlockFromItem(itemStack.getItem) == null) {
				GameData.getItemRegistry.getNameForObject(itemStack.getItem).asInstanceOf[String]
			}
			else {
				GameData.getBlockRegistry.getNameForObject(
					Block.getBlockFromItem(itemStack.getItem)
				).asInstanceOf[String]
			}
		val ui: UniqueIdentifier = new UniqueIdentifier(name)
		(if (hasID) ui.modId + ":" else "") + ui.name +
				(if (hasMeta) ":" + itemStack.getItemDamage else "")
	}

	def getName(state: IBlockState, hasID: Boolean, hasMeta: Boolean): String = {
		this.getName(
			new ItemStack(state.getBlock, 1, state.getBlock.getMetaFromState(state)),
			hasID, hasMeta
		)
	}

	def getItemStack(name: String): ItemStack = {
		if (!name.matches("(.*):(.*)")) return null
		var endNameIndex: Int = name.length
		var metadata: Int = OreDictionary.WILDCARD_VALUE

		if (name.matches("(.*):(.*):(.*)")) {
			endNameIndex = name.lastIndexOf(':')
			metadata = name.substring(endNameIndex + 1, name.length()).toInt
		}

		val modid: String = name.substring(0, name.indexOf(':'))
		val itemName: String = name.substring(name.indexOf(':') + 1, endNameIndex)
		val block: Block = GameRegistry.findBlock(modid, itemName)
		val item: Item = GameRegistry.findItem(modid, itemName)
		val itemStack: ItemStack = if (block != null) new ItemStack(block, 1, metadata)
		else if (item != null) new ItemStack(item, 1, metadata) else null
		itemStack
	}

	def getState(name: String): IBlockState = {
		val stack: ItemStack = this.getItemStack(name)
		val block: Block = Block.getBlockFromItem(stack.getItem)
		if (block != null) block.getStateFromMeta(stack.getItemDamage)
		else null
	}

	def isInCollection(itemStack: ItemStack, list: util.Collection[String]): Boolean = {
		list.contains(this.getName(itemStack, hasID = true, hasMeta = false)) ||
				list.contains(this.getName(itemStack, hasID = true, hasMeta = true))
	}

}
