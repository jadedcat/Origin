package com.temportalist.origin.wrapper.common.block

import java.util

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.world.World

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
 * @author CountryGamer
 */
class BlockWrapper(material: Material, val pluginID: String, name: String,
		itemBlock: Class[_ <: ItemBlock]) extends Block(material) {

	// Default Constructor
	this.setBlockName(name)
	if (itemBlock != null) {
		GameRegistry.registerBlock(this, itemBlock, name)
	}
	else {
		GameRegistry.registerBlock(this, name)
	}

	// End constructor

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

	/**
	 * Register the icons for this block
	 * @param iconRegister
	 */
	@SideOnly(Side.CLIENT)
	override def registerBlockIcons(iconRegister: IIconRegister) {
		// set this icon from the path found at function 'getTexturePath()'
		this.blockIcon = iconRegister.registerIcon(this.getTexturePath)
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

	override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer,
			side: Int, offsetX: Float, offsetY: Float, offsetZ: Float): Boolean = {
		super.onBlockActivated(world, x, y, z, player, side, offsetX, offsetY, offsetZ)
	}

	override def onEntityWalking(world: World, x: Int, y: Int, z: Int, entity: Entity) {
		super.onEntityWalking(world, x, y, z, entity)
	}

	override def getDrops(world: World, x: Int, y: Int, z: Int, metadata: Int,
			fortune: Int): util.ArrayList[ItemStack] = {
		super.getDrops(world, x, y, z, metadata, fortune)
	}

}
