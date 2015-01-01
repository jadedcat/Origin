package com.temportalist.origin.library.common.register

import net.minecraft.entity._
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.fml.common.registry.EntityRegistry

/**
 *
 *
 * @author TheTemportalist
 */
trait EntityRegister extends Register {

	var entID: Int = 0

	def addEntityMappings(): Unit = {}

	protected final def addEntity(entityClass: Class[_ <: Entity], entityName: String,
			mod: Object): Unit = {
		this.addEntity(entityClass, entityName, mod, 80, 3, sendsVelocityUpdates = false)
	}

	/**
	 * @param entityClass The entity class
	 * @param entityName A unique name for the entity
	 * @param mod The mod
	 * @param trackingRange The range at which MC will send tracking updates
	 * @param updateFrequency The frequency of tracking updates
	 * @param sendsVelocityUpdates Whether to send velocity information packets as well
	 */
	protected final def addEntity(entityClass: Class[_ <: Entity], entityName: String, mod: Object,
			trackingRange: Int, updateFrequency: Int, sendsVelocityUpdates: Boolean): Unit = {
		EntityRegistry.registerModEntity(entityClass, entityName, entID, mod,
			trackingRange, updateFrequency, sendsVelocityUpdates
		)

		entID += 1
	}

	def addEntitySpawns(): Unit = {}

	/**
	 *
	 * @param entityClass The class of the entity
	 * @param weightedProb The weighted probability that this entity will spawn
	 * @param min The minimum count of instances the spawn will call
	 * @param max The maximum count of instances the spawn will call
	 * @param typeOfCreature The creature type
	 * @param biomes A list of biomes that we can spawn in
	 */
	protected final def addSpawn(entityClass: Class[_ <: Entity],
			weightedProb: Int, min: Int, max: Int, typeOfCreature: EnumCreatureType,
			biomes: BiomeGenBase*): Unit = {
		EntityRegistry.addSpawn(entityClass, weightedProb, min, max, typeOfCreature, biomes)
	}

}
