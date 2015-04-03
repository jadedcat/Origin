package com.temportalist.origin.test

import com.temportalist.origin.library.common.network.IPacketOrigin
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
class PacketUpdateMode() extends IPacketOrigin {

	def this(newMode: ScrewdriverMode) {
		this()
		this.add(newMode.getName())
		//println ("Added mode " + newMode.getName())
	}

	override def handle(player: EntityPlayer, isServer: Boolean): Unit = {
		val name: String = this.get[String]
		//println("Triggering " + name)
		ScrewdriverMode.getMode(name).triggerSelection(player)
	}

}
