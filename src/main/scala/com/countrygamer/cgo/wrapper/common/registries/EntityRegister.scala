package com.countrygamer.cgo.wrapper.common.registries

import net.minecraft.entity.EntityList

/**
 *
 *
 * @author CountryGamer
 */
class EntityRegister() extends Register {

	override def register(): Unit = {}

	def addEntityMappings(): Unit = {}

	def addEntitySpawns(): Unit = {}

	def getNewEntityID(): Int = {
		var id: Int = 0
		while (EntityList.IDtoClassMapping.containsKey(id)) {
			id += 1
		}
		id
	}

}
