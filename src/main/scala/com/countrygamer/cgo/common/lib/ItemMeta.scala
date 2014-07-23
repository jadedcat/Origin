package com.countrygamer.cgo.common.lib

import net.minecraft.block.Block
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author CountryGamer
 */
class ItemMeta(var item: Item, var metadata: Int) {

	private var isBlock: Boolean = false

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this(block: Block, metadata: Int) {
		this(Item.getItemFromBlock(block), metadata)
		this.isBlock = true
	}
	// End Constructors

	def saveToNBT(compound: NBTTagCompound) {
		compound.setInteger("item_id", Item.getIdFromItem(this.item))
		compound.setInteger("metadata", this.metadata)
		compound.setBoolean("isBlock", this.isBlock)
	}

	def loadFromNBT(compound: NBTTagCompound) {
		this.item = Item.getItemById(compound.getInteger("item_id"))
		this.metadata = compound.getInteger("metadata")
		this.isBlock = compound.getBoolean("isBlock")
	}

	override def equals(obj: Any): Boolean = {
		return this.equals(obj, false)
	}

	def equals(obj: Any, ignoreMeta: Boolean): Boolean = {
		if (obj.isInstanceOf[ItemMeta]) {
			val that: ItemMeta = obj.asInstanceOf[ItemMeta]
			return (this.item == that.getItem) &&
					(if (ignoreMeta) true else this.metadata == that.getMetadata)
		}
		return false
	}

	def copy: ItemMeta = {
		if (this.isBlock) return new ItemMeta(Block.getBlockFromItem(this.item), this.metadata)
		return new ItemMeta(this.item, this.metadata)
	}

	def getItemStack(size: Int): ItemStack = {
		return new ItemStack(this.item, if (size < 1) 1 else (if (size > 64) 64 else size),
			this.metadata)
	}

	def print {
		System.out.println(this.item.getUnlocalizedName + " - " + this.metadata)
	}

	def getItem: Item = {
		return this.item
	}

	def getBlock: Block = {
		if (this.isBlock) {
			return Block.getBlockFromItem(this.item)
		}
		return null
	}

	def getMetadata: Int = {
		return this.metadata
	}

}
