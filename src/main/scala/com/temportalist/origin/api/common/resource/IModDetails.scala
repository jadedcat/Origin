package com.temportalist.origin.api.common.resource

import com.temportalist.origin.api.common.utility.{Scala, WorldHelper}
import com.temportalist.origin.foundation.common.network.IPacket
import com.temportalist.origin.internal.common.network.handler.Network

/**
 *
 *
 * @author  TheTemportalist  5/3/15
 */
trait IModDetails {

	def getModid: String

	def getModName: String

	def getModVersion: String

	final def registerPackets(messages: Class[_ <: IPacket]*): Unit = {
		if (WorldHelper.isClient)
			Network.registerHandler(this.getModid, Scala.toArrayList(messages.seq))
	}

}
