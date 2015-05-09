package com.temportalist.origin.foundation.common.network

import com.temportalist.origin.api.common.extended.ExtendedEntity
import com.temportalist.origin.internal.common.extended.ExtendedEntityHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

/**
 *
 *
 * @author TheTemportalist
 */
class PacketSyncExtendedProperties() extends IPacket {

	def this(extendedClass: Class[_ <: ExtendedEntity], data: NBTTagCompound) {
		this()
		this.add(ExtendedEntityHandler.getClassKey(extendedClass))
		this.add(data)
	}

	override def handle(player: EntityPlayer, side: Side): Unit = {
		ExtendedEntityHandler.getExtendedByKey(
			player, this.get[String]).loadNBTData(this.get[NBTTagCompound])
	}

}
