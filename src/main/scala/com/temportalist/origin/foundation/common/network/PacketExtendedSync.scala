package com.temportalist.origin.foundation.common.network

import com.temportalist.origin.foundation.common.extended.ExtendedEntity
import com.temportalist.origin.internal.common.extended.ExtendedEntityHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author  TheTemportalist  5/21/15
 */
class PacketExtendedSync() extends IPacket {

	def this(extendedClass: Class[_ <: ExtendedEntity], uniqueID: String) {
		this()
		this.add(ExtendedEntityHandler.getClassKey(extendedClass))
		this.add(uniqueID)
	}

	override def handle(player: EntityPlayer, side: Side): Unit = {
		val extended = ExtendedEntityHandler.getExtendedByKey(player, this.get[String])
		val id = this.get[String]
		if (!id.isEmpty) extended.handleSyncPacketData(id, this, side)
		else extended.loadNBTData(this.get[NBTTagCompound])
	}

}
