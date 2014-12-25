package com.temportalist.origin.wrapper.common.tile

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.lib.LogHelper
import net.minecraft.block.state.IBlockState
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraftforge.common.property.IUnlistedProperty
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier

/**
 *
 *
 * @author TheTemportalist
 */
trait ICamouflage {

	private var blockState: IBlockState = null

	def isCamouflaged: Boolean = {
		this.hasCamouflage
	}

	def hasCamouflage: Boolean = {
		this.blockState != null
	}

	def getCamouflage: IBlockState = this.blockState

	def setCamouflage(blockState: IBlockState): Unit = {
		this.blockState = blockState
	}

	def saveCamouflageNBT(tagCom: NBTTagCompound): Unit = {

		tagCom.setBoolean("ICamouflage_hasCamouflage", this.hasCamouflage)
		if (this.hasCamouflage) {
			val ui: UniqueIdentifier = GameRegistry
					.findUniqueIdentifierFor(this.blockState.getBlock)
			tagCom.setString("ICamouflage_modName", ui.modId)
			tagCom.setString("ICamouflage_blockName", ui.name)
			tagCom.setInteger(
				"ICamouflage_blockMeta", blockState.getBlock.getMetaFromState(blockState)
			)
		}

	}

	def readCamouflageNBT(tagCom: NBTTagCompound): Unit = {

		if (tagCom.getBoolean("ICamouflage_hasCamouflage")) {
			try {
				this.blockState = GameRegistry.findBlock(
					tagCom.getString("ICamouflage_modName"),
					tagCom.getString("ICamouflage_blockName")
				).getStateFromMeta(tagCom.getInteger("ICamouflage_blockMeta"))
			} catch {
				case e: Exception =>
					if (e.isInstanceOf[NullPointerException]) {
						LogHelper.info(Origin.pluginName,
							"No block for " + tagCom.getString("ICamouflage_modName") + ":" +
									tagCom.getString("ICamouflage_blockName"))
					}
					else e.printStackTrace()
			}
		}

	}

}

object ICamouflage {

	val CAMO_PROP: IUnlistedProperty[BlockPos] = new IUnlistedProperty[BlockPos] {
		override def getType: Class[BlockPos] = classOf[BlockPos]

		override def getName: String = "Block_Pos"

		override def valueToString(value: BlockPos): String = value.toString

		override def isValid(pos: BlockPos): Boolean = {
			pos.getX() >= -30000000 &&
					pos.getZ() >= -30000000 &&
					pos.getX() < 30000000 &&
					pos.getZ() < 30000000 &&
					pos.getY() >= 0 &&
					pos.getY() < 256
		}
	}

}
