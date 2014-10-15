package com.countrygamer.cgo.library.common.lib

import net.minecraft.entity.Entity
import net.minecraft.world.{Teleporter, WorldServer}

/**
 *
 *
 * @author CountryGamer
 */
class TeleporterCore(ws: WorldServer) extends Teleporter(ws) {

	override def makePortal(entity: Entity): Boolean = {
		true
	}

	override def placeInExistingPortal(entity: Entity, x: Double, y: Double, z: Double,
			f: Float): Boolean = {
		true
	}

}
