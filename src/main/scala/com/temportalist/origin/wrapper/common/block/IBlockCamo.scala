package com.temportalist.origin.wrapper.common.block

import com.temportalist.origin.wrapper.common.tile.ICamouflage
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.IExtendedBlockState

/**
 *
 *
 * @author TheTemportalist
 */
trait IBlockCamo {

	/**
	 * Call in Block.getExtendedState(IBlockState, IBlockAccess, BlockPos)
	 * @param state The actual state as from Block.getActualState(IBlockState, IBlockAccess, BlockPos)
	 * @param world
	 * @param pos
	 * @return
	 */
	def getBlockState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState =
		state.asInstanceOf[IExtendedBlockState].withProperty(ICamouflage.CAMO_PROP, pos)

	/**
	 * Call in Block.createBlockState()
	 * @return
	 */
	def createState(block: Block): IBlockState = null // todo
		//new ExtendedBlockState(block, new Array[IProperty](0), new Array[IUnlistedProperty[_]](1))

}
