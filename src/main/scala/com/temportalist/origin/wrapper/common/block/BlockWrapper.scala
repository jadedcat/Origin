package com.temportalist.origin.wrapper.common.block

import java.util

import com.temportalist.origin.wrapper.common.IRenderingObject
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemBlock, ItemStack}
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 * A wrapper class for the Minecraft Block.
 *
 * @param material
 * The block material (@see Material)
 * @param pluginID
 * The plugin id for the owner plugin
 * @param name
 * The name of the block
 * @param itemBlock
 * The item block class
 *
 * @author TheTemportalist
 */
class BlockWrapper(material: Material, val pluginID: String, name: String,
		itemBlock: Class[_ <: ItemBlock]) extends Block(material) with IRenderingObject {

	this.setUnlocalizedName(name)
	if (itemBlock != null) {
		GameRegistry.registerBlock(this, itemBlock, name)
	}
	else {
		GameRegistry.registerBlock(this, name)
	}
	this.initRendering()

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

	override def getItem(): Item = Item.getItemFromBlock(this)

	@SideOnly(Side.CLIENT)
	override def getModelLocation(): ModelResourceLocation = {
		new ModelResourceLocation(this.pluginID + ":" + this.name, "inventory")
	}

	/**
	 * Get the texture path of the block's icon
	 * @return
	 * The path of this block's icon
	 */
	protected def getTexturePath: String = {
		// Get the unlocalized name of this block
		val unlocalizedName: String = this.getUnlocalizedName
		// return the result of the unlocalized name, making sure to get rid of the "tile." prefix
		unlocalizedName.substring(unlocalizedName.indexOf(".") + 1)
	}

	/**
	 * Get the non-local name of this block
	 * @return
	 */
	override def getUnlocalizedName: String = {
		// return a formatted string using the format:
		//   tile.{pluginID}:{blockName}
		String.format("tile.%s%s", this.pluginID + ":",
			this.getUnwrappedUnlocalizedName(super.getUnlocalizedName))
	}

	/**
	 * Unwrap the passed string's name (gets rid of the prefix)
	 * @param unlocalizedName
	 * @return
	 */
	def getUnwrappedUnlocalizedName(unlocalizedName: String): String = {
		// Get rid of the "tile." prefix and return the result
		unlocalizedName.substring(unlocalizedName.indexOf(".") + 1)
	}

	// ~~~~~~~~~~~~~~~ Start supered wrappers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState,
			playerIn: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float,
			hitZ: Float): Boolean = {
		super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ)
	}

	override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState,
			fortune: Int): util.List[ItemStack] = {
		super.getDrops(world, pos, state, fortune)
	}
}
