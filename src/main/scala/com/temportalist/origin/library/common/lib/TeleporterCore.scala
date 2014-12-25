package com.temportalist.origin.library.common.lib

import net.minecraft.entity.Entity
import net.minecraft.world.{Teleporter, WorldServer}

/**
 *
 *
 * @author TheTemportalist
 */
class TeleporterCore(ws: WorldServer) extends Teleporter(ws) {

	override def makePortal(entity: Entity): Boolean = {
		true
	}

	// todo check this function
	override def func_180620_b(entity: Entity, f: Float): Boolean = {
		true
	}

}
