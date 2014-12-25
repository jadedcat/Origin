package com.temportalist.origin.wrapper.common.block

import java.util
import java.util.Random

import com.temportalist.origin.library.common.utility.Drops
import com.temportalist.origin.wrapper.common.tile.{ICustomDrops, IPowerable}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
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
	 * @param state
	 * @return
	 */
	override def createTileEntity(world: World, state: IBlockState): TileEntity = {
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
		null
	}

	/**
	 * Check to see if this block has a tile entity
	 * @param metadata
	 * @return
	 */
	override def hasTileEntity(state: IBlockState): Boolean = {
		this.tileEntityClass != null
	}

	// ~~~~~~~~~~~~~~~ Start supered wrappers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
		// Check the power as soon as self is added to world
		this.checkPower(worldIn, pos)
		super.onBlockAdded(worldIn, pos, state)
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
	override def onNeighborBlockChange(worldIn: World, pos: BlockPos, state: IBlockState,
			neighborBlock: Block): Unit = {
		// check the powewr when nearby blocks change
		this.checkPower(worldIn, pos)
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock)
	}

	override def updateTick(worldIn: World, pos: BlockPos, state: IBlockState,
			rand: Random): Unit = {
		// check the power when updates are scheduled
		this.checkPower(worldIn, pos)
		super.updateTick(worldIn, pos, state, rand)
	}

	def checkPower(world: World, pos: BlockPos) {
		// make sure we are server side
		if (!world.isRemote) {
			// get the tile entity
			val tileEntity: TileEntity = world.getTileEntity(pos)
			// check if it is a valid powerable tile entity
			if (tileEntity != null && tileEntity.isInstanceOf[IPowerable]) {
				// cast and store in variable
				val powerable: IPowerable = tileEntity.asInstanceOf[IPowerable]
				// get if the world says this block is powered
				val blockGettingPower: Boolean = world.isBlockPowered(pos)
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
	def hasTileEntityDrops(state: IBlockState): Boolean = {
		false
	}

	override def breakBlock(worldIn: World, pos: BlockPos, state: IBlockState): Unit = {
		// get the super's drops (gets this block as a drop)
		val drops: util.List[ItemStack] = this.getDrops(worldIn, pos, state, 0)
		// get the tile entity
		val tileEntity: TileEntity = worldIn.getTileEntity(pos)

		// check if valid tile entity
		if (tileEntity != null) tileEntity match {
			case cuDo: ICustomDrops =>
				if (this.hasTileEntityDrops(state)) cuDo.getDrops(drops, state.getBlock, state)
		}

		// Spawn the drops
		Drops.spawnDrops(worldIn, pos: BlockPos, drops)
		// remove the tile
		worldIn.removeTileEntity(pos)

		super.breakBlock(worldIn, pos, state)
	}

}
