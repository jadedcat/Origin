package com.temportalist.origin.library.common.nethandler

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author CountryGamer
 */
trait IPacket {

	def writeTo_do(buffer: ByteBuf): Unit = {
		this.writeTo(buffer)
	}

	def writeTo(buffer: ByteBuf): Unit

	def readFrom(buffer: ByteBuf): Unit

	def handleOnClient(player: EntityPlayer): Unit = {
		this.handle(player)
	}

	def handleOnServer(player: EntityPlayer): Unit = {
		this.handle(player)
	}

	def handle(player: EntityPlayer): Unit = {}

}
