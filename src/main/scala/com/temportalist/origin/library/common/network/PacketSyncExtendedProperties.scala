package com.temportalist.origin.library.common.network

import java.util

import com.temportalist.origin.library.common.nethandler.IPacket
import com.temportalist.origin.wrapper.common.extended.{ExtendedEntity, ExtendedEntityHandler}
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils

/**
 *
 *
 * @author TheTemportalist
 */
class PacketSyncExtendedProperties(var extendedClass: Class[_ <: ExtendedEntity],
		var data: NBTTagCompound) extends IPacket {

	// Default Constructor

	// End Constructor

	// Other Constructors
	def this() {
		this(null, null)
	}

	// End Constructors

	override def writeTo(buffer: ByteBuf): Unit = {
		ByteBufUtils.writeTag(buffer, data)
		ByteBufUtils.writeUTF8String(buffer,
			ExtendedEntityHandler.getExtendedProperties.get(this.extendedClass)(0))

	}

	override def readFrom(buffer: ByteBuf): Unit = {
		this.data = ByteBufUtils.readTag(buffer)
		val key: String = ByteBufUtils.readUTF8String(buffer)
		this.extendedClass = this.getClassWithKey(key)

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

	override def handleOnClient(player: EntityPlayer): Unit = {
		this.handleSync(player)

	}

	override def handleOnServer(player: EntityPlayer): Unit = {
		this.handleSync(player)

	}

	def handleSync(player: EntityPlayer): Unit = {
		ExtendedEntityHandler.getExtended(player, this.extendedClass).loadNBTData(this.data)
	}

}
