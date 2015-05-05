package com.temportalist.origin.foundation.common.network

import java.util

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
		this.add(ExtendedEntityHandler.getExtendedProperties.get(extendedClass)(0))
		this.add(data)
	}

	override def handle(player: EntityPlayer, side: Side): Unit = {
		ExtendedEntityHandler.getExtended(
			player, this.getClassWithKey(this.get[String])
		).loadNBTData(this.get[NBTTagCompound])
	}

	private def getClassWithKey(key: String): Class[_ <: ExtendedEntity] = {
		val map: util.HashMap[Class[_ <: ExtendedEntity], Array[String]] = ExtendedEntityHandler
				.getExtendedProperties
		val iterator: util.Iterator[Class[_ <: ExtendedEntity]] = map.keySet().iterator()
		while (iterator.hasNext) {
			val extendedClass: Class[_ <: ExtendedEntity] = iterator.next()
			if (map.get(extendedClass)(0).equals(key)) {
				return extendedClass
			}
		}
		null
	}

}
