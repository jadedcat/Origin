package com.temportalist.origin.library.common.network

import com.temportalist.origin.library.common.nethandler.IPacket
import com.temportalist.origin.library.common.Origin

/**
 *
 *
 * @author TheTemportalist 4/2/15
 */
trait IPacketOrigin extends IPacket {
	override def getChannel(): String = Origin.MODID
}
