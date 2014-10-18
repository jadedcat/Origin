package com.temportalist.origin.wrapper.common.block

import java.util
import java.util.Random

import com.temportalist.origin.library.common.utility.Drops
import com.temportalist.origin.wrapper.common.tile.{ICustomDrops, IPowerable}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class BlockWrapperTE(material: Material, pluginID: String, name: String,
		itemBlock: Class[_ <: ItemBlock], val tileEntityClass: Class[_ <: TileEntity])
		extends BlockWrapper(material, pluginID, name, itemBlock) {

	// Default Constructor
	this.isBlockContainer = true

	// End Constructor

	// Other Constructors
	def this(material: Material, pluginID: String, name: String,
			tileEntityClass: Class[_ <: TileEntity]) {
		this(material, pluginID, name, null, tileEntityClass)

	}

	def this(pluginID: String, name: String, itemBlock: Class[_ <: ItemBlock],
			tileEntityClass: Class[_ <: TileEntity]) {
		this(Material.ground, pluginID, name, itemBlock, tileEntityClass)

	}

	def this(pluginID: String, name: String, tileEntityClass: Class[_ <: TileEntity]) {
		this(pluginID, name, null, tileEntityClass)

	}

	// End Constructors

	/**
	 * Called to create a new tile entity instance for this block
	 * @param world
	 * @param metadata
	 * @return
	 */
	override def createTileEntity(world: World, metadata: Int): TileEntity = {
		if (this.tileEntityClass != null) {
			try {
				// Try to create a new instance of this tile entity's class
				return this.tileEntityClass.newInstance()
			}
			catch {
				case e: InstantiationException =>
					e.printStackTrace
				case e: IllegalAccessException =>
					e.printStackTrace
			}
		}
		// Will return a null value
		super.createTileEntity(world, metadata)
	}

	/**
	 * Check to see if this block has a tile entity
	 * @param metadata
	 * @return
	 */
	override def hasTileEntity(metadata: Int): Boolean = {
		this.tileEntityClass != null
	}

	// ~~~~~~~~~~~~~~~ Start supered wrappers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def onBlockAdded(world: World, x: Int, y: Int, z: Int): Unit = {
		// Check the power as soon as self is added to world
		this.checkPower(world, x, y, z)
		super.onBlockAdded(world, x, y, z)
	}

	/**
	 * Lets the block know when one of its neighbor changes.
	 * Doesn't know which neighbor changed (coordinates passed are their own)
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param block the neighbor block
	 */
	override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block): Unit = {
		// check the powewr when nearby blocks change
		this.checkPower(world, x, y, z)
	}

	override def updateTick(world: World, x: Int, y: Int, z: Int, random: Random): Unit = {
		// check the power when updates are scheduled
		this.checkPower(world, x, y, z)
	}

	def checkPower(world: World, x: Int, y: Int, z: Int) {
		// make sure we are server side
		if (!world.isRemote) {
			// get the tile entity
			val tileEntity: TileEntity = world.getTileEntity(x, y, z)
			// check if it is a valid powerable tile entity
			if (tileEntity != null && tileEntity.isInstanceOf[IPowerable]) {
				// cast and store in variable
				val powerable: IPowerable = tileEntity.asInstanceOf[IPowerable]
				// get if the world says this block is powered
				val blockGettingPower: Boolean = world.isBlockIndirectlyGettingPowered(x, y, z)
				// set the powerable's power based on world power get ^
				if (powerable.isPowered(checkState = false) && !blockGettingPower) {
					powerable.setPowered(isRecievingPower = false)
				}
				else if (!powerable.isPowered(checkState = false) && blockGettingPower) {
					powerable.setPowered(isRecievingPower = true)
				}
			}
		}
	}

	/**
	 * Allows for drops from an ICustomDrops tile entity
	 * @param metadata
	 * @return
	 */
	def hasTileEntityDrops(metadata: Int): Boolean = {
		false
	}

	override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
		// get the super's drops (gets this block as a drop)
		val drops: util.ArrayList[ItemStack] = this.getDrops(world, x, y, z, meta, 0)

		// get the tile entity
		val tileEntity: TileEntity = world.getTileEntity(x, y, z)
		// check if valid tile entity
		if (tileEntity != null && tileEntity.isInstanceOf[ICustomDrops]) {
			// check if we are allowed to drop things
			if (this.hasTileEntityDrops(meta)) {
				// cast and run the get drops method
				tileEntity.asInstanceOf[ICustomDrops].getDrops(drops, block, meta)
			}
		}
		// Spawn the drops
		Drops.spawnDrops(world, x, y, z, drops)
		// remove the tile
		world.removeTileEntity(x, y, z)

	}

}
