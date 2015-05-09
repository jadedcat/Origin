package com.temportalist.origin.api.common.extended

import com.temportalist.origin.api.common.utility.WorldHelper
import com.temportalist.origin.foundation.common.network.{IPacket, PacketSyncExtendedProperties}
import cpw.mods.fml.relauncher.Side
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
abstract class ExtendedEntity(private var entity: EntityPlayer)
		extends IExtendedEntityProperties {

	override def init(entity: Entity, world: World): Unit = {}

	def saveNBTData(tagCom: NBTTagCompound): Unit

	def loadNBTData(tagCom: NBTTagCompound): Unit

	def syncEntity(): Unit = this.getSyncPacket.sendToOpposite(WorldHelper.getSide)

	def syncEntity(side: Side): Unit =
		if (side.isClient) this.getSyncPacket.sendToClients() else this.getSyncPacket.sendToServer()

	private def getSyncPacket: IPacket = {
		val tagCom: NBTTagCompound = new NBTTagCompound()
		this.saveNBTData(tagCom)
		new PacketSyncExtendedProperties(this.getClass, tagCom)
	}

}
