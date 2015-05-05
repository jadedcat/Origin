package com.temportalist.origin.internal.common.extended

import java.util

import com.temportalist.origin.api.common.extended.ExtendedEntity
import com.temportalist.origin.api.common.lib.LogHelper
import com.temportalist.origin.internal.common.Origin
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.IExtendedEntityProperties
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.{EntityEvent, EntityJoinWorldEvent}

/**
 *
 *
 * @author TheTemportalist
 */
object ExtendedSync {

	val persistanceTags: util.HashMap[Class[_ <: ExtendedEntity], util.HashMap[String, NBTTagCompound]] = new
					util.HashMap[Class[_ <: ExtendedEntity], util.HashMap[String, NBTTagCompound]]()

	def storeEntityData(extendedClass: Class[_ <: ExtendedEntity], player: EntityPlayer,
			data: NBTTagCompound) {
		ExtendedSync.checkForClassKey(extendedClass)
		val playerName: String = player.getGameProfile.getName
		ExtendedSync.persistanceTags.get(extendedClass).put(playerName, data)
	}

	def getEntityData(extendedClass: Class[_ <: ExtendedEntity],
			player: EntityPlayer): NBTTagCompound = {
		if (ExtendedSync.checkForClassKey(extendedClass)) {
			val playerName: String = player.getGameProfile.getName
			return ExtendedSync.persistanceTags.get(extendedClass).remove(playerName)
		}
		null
	}

	private def checkForClassKey(extendedClass: Class[_ <: ExtendedEntity]): Boolean = {
		if (!ExtendedSync.persistanceTags.containsKey(extendedClass)) {
			ExtendedSync.persistanceTags
					.put(extendedClass, new util.HashMap[String, NBTTagCompound])
			return false
		}
		true
	}

	/**
	 * Control the creation of ExtendedEntities for each player (only if they do not already have
	 * that EntendedEntity)
	 *
	 * @param event
	 */
	@SubscribeEvent
	def onEntityConstructing(event: EntityEvent.EntityConstructing) {
		if (event.entity != null && event.entity.isInstanceOf[EntityPlayer]) {
			val extendedProperties: util.HashMap[Class[_ <: ExtendedEntity], Array[String]] =
				ExtendedEntityHandler.getExtendedProperties

			val iterator: util.Iterator[Class[_ <: ExtendedEntity]] = extendedProperties.keySet()
					.iterator()
			while (iterator.hasNext) {
				val extendedClass: Class[_ <: ExtendedEntity] = iterator.next()

				val props: IExtendedEntityProperties = event.entity
						.getExtendedProperties(extendedProperties.get(extendedClass)(0))
				if (props == null) {
					ExtendedEntityHandler
							.registerPlayer(event.entity.asInstanceOf[EntityPlayer], extendedClass)
				}

			}

		}
	}

	/**
	 * Handles the saving of ExtendedEntity data when an entity dies
	 *
	 * @param event
	 */
	@SubscribeEvent
	def onLivingDeath(event: LivingDeathEvent) {
		if (event.entity == null || event.entity.worldObj.isRemote ||
				!event.entity.isInstanceOf[EntityPlayer]) return
		val player: EntityPlayer = event.entity.asInstanceOf[EntityPlayer]
		val propertyMap: util.Map[Class[_ <: ExtendedEntity], Array[String]] = ExtendedEntityHandler
				.getExtendedProperties

		val iterator: util.Iterator[_] = propertyMap.keySet().iterator()
		while (iterator.hasNext) {
			val extendedClass: Class[_ <: ExtendedEntity] = iterator.next()
					.asInstanceOf[Class[_ <: ExtendedEntity]]
			val shouldPersist: String = propertyMap.get(extendedClass)(1)
			if (shouldPersist.toBoolean) {
				val extendedPlayer: ExtendedEntity = ExtendedEntityHandler
						.getExtended(player, extendedClass).asInstanceOf[ExtendedEntity]
				val extPlayerData: NBTTagCompound = new NBTTagCompound
				extendedPlayer.saveNBTData(extPlayerData)
				ExtendedSync.storeEntityData(extendedClass, player, extPlayerData)
			}
		}

	}

	/**
	 * Handles retrieving a player's ExtendedEntity data, after they have died.
	 *
	 * @param event
	 */
	@SubscribeEvent
	def onEntityJoinWorld(event: EntityJoinWorldEvent) {
		if (event.entity != null) {
			event.entity match {
				case player: EntityPlayer =>
					val propMap: util.Map[Class[_ <: ExtendedEntity], Array[String]] =
						ExtendedEntityHandler.getExtendedProperties
					val iterPropMap: util.Iterator[Class[_ <: ExtendedEntity]] =
						propMap.keySet().iterator()
					while (iterPropMap.hasNext) {
						val extClass: Class[_ <: ExtendedEntity] = iterPropMap.next()
						var extPlayer: ExtendedEntity = ExtendedEntityHandler.getExtended(
							player, extClass
						).asInstanceOf[ExtendedEntity]
						if (extPlayer == null) {
							ExtendedEntityHandler.registerPlayer(player, extClass)
							extPlayer = ExtendedEntityHandler.getExtended(
								player, extClass
							).asInstanceOf[ExtendedEntity]
						}
						if (extPlayer == null) {
							try {
								if (propMap.get(extClass)(1).toBoolean) {
									val extPlayerData: NBTTagCompound =
										ExtendedSync.getEntityData(extClass, player)
									if (extPlayerData != null) {
										extPlayer.loadNBTData(extPlayerData)
									}
								}
								extPlayer.syncEntity()
							}
							catch {
								case e: Exception => e.printStackTrace()
							}
						}

					}
				case _ =>
			}
		}
		else {
			LogHelper.info(Origin.MODNAME, "ERROR, null entity joined world")
		}
	}

}