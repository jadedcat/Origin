package com.temportalist.origin.test

import com.temportalist.origin.foundation.common.network.IPacket
import cpw.mods.fml.relauncher.Side
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
class PacketUpdateMode() extends IPacket {

	def this(newMode: ScrewdriverMode) {
		this()
		this.add(newMode.getName())
		//println ("Added mode " + newMode.getName())
	}

	override def handle(player: EntityPlayer, side: Side): Unit = {
		val name: String = this.get[String]
		//println("Triggering " + name)
		ScrewdriverMode.getMode(name).triggerSelection(player)
	}

}
