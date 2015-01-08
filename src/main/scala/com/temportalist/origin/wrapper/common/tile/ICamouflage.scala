package com.temportalist.origin.wrapper.common.tile

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.lib.{LogHelper, NameParser}
import com.temportalist.origin.library.common.utility.States
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.property.IUnlistedProperty

/**
 *
 *
 * @author TheTemportalist
 */
trait ICamouflage extends TileEntity {

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

		tagCom.setBoolean("ICamouflage_has", this.hasCamouflage)
		if (this.hasCamouflage) {
			tagCom.setString("ICamouflage_name", States.getNameFromState(this.getCamouflage))
		}

	}

	def readCamouflageNBT(tagCom: NBTTagCompound): Unit = {

		if (tagCom.getBoolean("ICamouflage_has")) {
			try {
				this.blockState = States.getStateFromName(tagCom.getString("ICamouflage_name"))
			} catch {
				case e: Exception =>
					if (e.isInstanceOf[NullPointerException]) {
						LogHelper.info(Origin.pluginName,
							"No block for " + tagCom.getString("ICamouflage_name")
						)
					}
					else e.printStackTrace()
			}
		}

	}

}

object ICamouflage {

	val CAMO_PROP: IUnlistedProperty[String] = new IUnlistedProperty[String] {
		override def getType: Class[String] = classOf[String]

		override def getName: String = "String"

		override def valueToString(value: String): String = value

		override def isValid(value: String): Boolean = {
			NameParser.getItemStack(value) != null
		}

	}

	def getCamoString(itemStack: ItemStack): String = {
		if (itemStack.hasTagCompound && itemStack.getTagCompound.getBoolean("ICamouflage_has"))
			itemStack.getTagCompound.getString("ICamouflage_name")
		else null
	}

}
