package com.temportalist.origin.wrapper.common.tile

import com.temportalist.origin.library.common.lib.vec.BlockCoord
import com.temportalist.origin.wrapper.common.inventory.IInv
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity

/**
 * A wrapper class for Minecraft's TileEntity
 *
 * @param name
 * The name of this tile entity, this is superficial, used normally only for inventories
 *
 * @author TheTemportalist
 */
class TEWrapper(var name: String) extends TileEntity() with IInv with ITank with IPowerable {

	def this() {
		this("")
	}

	/**
	 * Returns the name of this tile entity
	 * @return
	 */
	def getName: String = {
		this.name
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Write/Read NBT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def updateTile(): Unit = this.markforUpdate()

	override def writeToNBT(tagCom: NBTTagCompound): Unit = {
		super.writeToNBT(tagCom)

		tagCom.setString("TEWrapper_teName", this.name)

		val invTag: NBTTagCompound = new NBTTagCompound
		this.toNBT_IInv(invTag)
		tagCom.setTag("inventory", invTag)

		val tanksTag: NBTTagCompound = new NBTTagCompound
		this.toNBT_ITank(tanksTag)
		tagCom.setTag("tanks", tanksTag)

	}

	override def readFromNBT(tagCom: NBTTagCompound): Unit = {
		super.readFromNBT(tagCom)

		this.name = tagCom.getString("TEWrapper_teName")

		this.fromNBT_IInv(tagCom.getCompoundTag("inventory"))

		this.fromNBT_ITank(tagCom.getCompoundTag("tanks"))

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Other ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def markDirty: Unit = {

		val thisCoord: BlockCoord = new BlockCoord(this)
		thisCoord.scheduleUpdate()
		thisCoord.markForUpdate()

	}

	override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
		this.readFromNBT(pkt.getNbtCompound)
	}

	override def getDescriptionPacket: Packet = {
		val tagCom: NBTTagCompound = new NBTTagCompound
		this.writeToNBT(tagCom)
		new S35PacketUpdateTileEntity(this.getPos, this.getBlockMetadata, tagCom)
	}

	def markforUpdate(): Unit = {
		this.getWorld.markBlockForUpdate(this.getPos)
	}

}
