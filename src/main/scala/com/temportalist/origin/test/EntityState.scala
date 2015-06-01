package com.temportalist.origin.test

import net.minecraft.entity.{EntityList, Entity}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class EntityState() {

	var owner: String = null
	var entityString: String = null
	var name: String = null
	private var entityNBT: NBTTagCompound = null
	private var entityInstance: Entity = null

	def setEntityNBT(nbt: NBTTagCompound): Unit = {
		this.entityNBT = nbt
	}

	def getEntityNBT: NBTTagCompound = this.entityNBT

	def hasCreatedEntity: Boolean = this.entityInstance != null

	def createEntity(world: World): Unit = if (this.entityNBT != null)
		this.entityInstance = EntityList.createEntityFromNBT(this.entityNBT, world)

	def getEntity: Entity = this.entityInstance

	def setEntity(e: Entity): Unit = this.entityInstance = e

	def register(): Unit = {
		Sonic.entityStates(this.entityString) = this
	}

	override def hashCode(): Int = {
		var hash: Int = 1
		hash = hash * 31 + this.owner.hashCode
		hash = hash * 31 + this.entityString.hashCode
		hash = hash * 31 + this.name.hashCode
		hash
	}

	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case state: EntityState =>
				return this.owner.equals(state.owner) &&
						this.entityString.equals(state.entityString) &&
						this.name.equals(this.name)
			case _ =>
		}
		false
	}

	override def toString: String = {
		"EntityState{ " +
				"owner:" + this.owner +
				",entityString:" + this.entityString +
				",name:" + this.name +
				",nbt:" + this.entityNBT +
				" }"
	}

}
