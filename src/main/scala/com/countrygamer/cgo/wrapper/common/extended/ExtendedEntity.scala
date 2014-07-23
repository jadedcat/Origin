package com.countrygamer.cgo.wrapper.common.extended

import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.common.network.PacketSyncExtendedProperties
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties
import net.minecraft.entity.Entity

/**
 *
 *
 * @author CountryGamer
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


		val syncMessage: PacketSyncExtendedProperties = new
						PacketSyncExtendedProperties(this.getClass, tagCom)

		Origin.proxy.syncPacket(syncMessage, this.player)

	}

}
