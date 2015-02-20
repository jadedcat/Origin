package com.temportalist.origin.wrapper.common.block

import java.util

import com.temportalist.origin.library.common.utility.{Drops, ItemRenderingHelper}
import com.temportalist.origin.wrapper.common.rendering.IRenderingObject
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * A wrapper class for the Minecraft Block.
 *
 * @param material
 * The block material (@see Material)
 * @param modid
 * The plugin id for the owner plugin
 * @param name
 * The name of the block
 * @param itemBlock
 * The item block class
 *
 * @author TheTemportalist
 */
class BlockWrapper(material: Material, val modid: String, name: String,
		itemBlock: Class[_ <: ItemBlock]) extends Block(material) with IRenderingObject {

	this.setUnlocalizedName(name)
	if (itemBlock != null) {
		GameRegistry.registerBlock(this, itemBlock, name)
	}
	else {
		GameRegistry.registerBlock(this, name)
	}
	ItemRenderingHelper.register(this)

	// Other Constructors
	def this(material: Material, pluginID: String, name: String) {
		this(material, pluginID, name, null)
	}

	def this(pluginID: String, name: String, itemBlock: Class[_ <: ItemBlock]) {
		this(Material.ground, pluginID, name, itemBlock)
	}

	def this(pluginID: String, name: String) {
		this(pluginID, name, null)
	}

	// End Constructors

	override def getBlock(): Block = this

	override def getItem(): Item = Item.getItemFromBlock(this)

	override def getCompoundName(): String = this.modid + ":" + this.name

	/**
	 * Get the non-local name of this block
	 * @return
	 */
	override def getUnlocalizedName: String = {
		// return a formatted string using the format:
		//   tile.{pluginID}:{blockName}
		"tile." + this.getCompoundName()
	}

	// ~~~~~~~~~~~~~~~ Start supered wrappers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState,
			playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float,
			hitZ: Float): Boolean = {
		super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ)
	}

	override def removedByPlayer(world: World, pos: BlockPos, player: EntityPlayer,
			willHarvest: Boolean): Boolean = {
		if (!player.capabilities.isCreativeMode)
			Drops.spawnDrops(world, pos,
				this.getDrops_Pre(world, pos, world.getBlockState(pos), world.getTileEntity(pos))
			)
		super.removedByPlayer(world, pos, player, willHarvest)
	}

	def getDrops_Pre(world: World, pos: BlockPos,
			state: IBlockState, tile: TileEntity): util.List[ItemStack] = {
		super.getDrops(world, pos, state, 0)
	}

	/* Runs on POST block destruction */
	override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState,
			fortune: Int): util.List[ItemStack] = new util.ArrayList[ItemStack]()

}
