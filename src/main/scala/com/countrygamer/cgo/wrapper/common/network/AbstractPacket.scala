package com.countrygamer.cgo.wrapper.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author CountryGamer
 */
trait AbstractPacket {

	def writeTo_do(buffer: ByteBuf): Unit = {
		this.writeTo(buffer)
	}

	def writeTo(buffer: ByteBuf): Unit

	def readFrom(buffer: ByteBuf): Unit

	def handleOnClient(player: EntityPlayer): Unit

	def handleOnServer(player: EntityPlayer): Unit

}
