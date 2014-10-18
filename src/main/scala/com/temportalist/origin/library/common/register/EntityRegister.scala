package com.temportalist.origin.library.common.register

import net.minecraft.entity.EntityList

/**
 *
 *
 * @author TheTemportalist
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
