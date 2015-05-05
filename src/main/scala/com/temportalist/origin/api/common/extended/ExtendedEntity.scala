package com.temportalist.origin.api.common.extended

import com.temportalist.origin.api.common.lib.LogHelper
import com.temportalist.origin.foundation.common.network.PacketSyncExtendedProperties
import com.temportalist.origin.internal.common.Origin
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties

/**
 *
 *
 * @author TheTemportalist
 */
abstract class ExtendedEntity(var player: EntityPlayer) extends IExtendedEntityProperties {

	// Default Constructor

	// End Constructor

	// Other Constructors

	// End Constructors

	override def init(entity: Entity, world: World): Unit = {
	}

	def saveNBTData(tagCom: NBTTagCompound): Unit

	def loadNBTData(tagCom: NBTTagCompound): Unit

	def syncEntity(): Unit = {
		val tagCom: NBTTagCompound = new NBTTagCompound()
		this.saveNBTData(tagCom)
		val syncMessage: PacketSyncExtendedProperties =
			new PacketSyncExtendedProperties(this.getClass, tagCom)
		if (this.player != null)
			Origin.proxy.syncPacket(syncMessage, this.player)
		else
			LogHelper.info(Origin.MODNAME, "Error: Null player in extended entity")
	}

}