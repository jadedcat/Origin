package com.temportalist.origin.wrapper.common.block

import com.temportalist.origin.library.common.lib.NameParser
import com.temportalist.origin.wrapper.common.tile.ICamouflage
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.property.{IExtendedBlockState, ExtendedBlockState, IUnlistedProperty}

/**
 *
 *
 * @author TheTemportalist
 */
trait ICamo extends Block {

	override def createBlockState(): BlockState =
		new ExtendedBlockState(
			this, new Array[IProperty](0), new Array[IUnlistedProperty[_]](1)
		)

	override def getExtendedState(
			state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = {
		world.getTileEntity(pos) match {
			case camoTile: ICamouflage =>
				val block: Block = camoTile.getCamouflage.getBlock
				val meta: Int = block.getMetaFromState(camoTile.getCamouflage)
				val name: String = NameParser.getName(
					new ItemStack(block, meta, 1), hasID = true, hasMeta = true
				)

				return state.asInstanceOf[IExtendedBlockState].withProperty(
					ICamouflage.CAMO_PROP, name
				)
			case _ =>
		}
		state
	}

}
