package com.temportalist.origin.library.common.network

import java.util

import com.temportalist.origin.library.common.nethandler.IPacket
import com.temportalist.origin.wrapper.common.extended.{ExtendedEntity, ExtendedEntityHandler}
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

	override def handle(player: EntityPlayer, isServer: Boolean): Unit = {
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
