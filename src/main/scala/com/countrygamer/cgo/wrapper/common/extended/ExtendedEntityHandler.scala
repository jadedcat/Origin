package com.countrygamer.cgo.wrapper.common.extended

import java.lang.reflect.InvocationTargetException
import java.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.IExtendedEntityProperties

/**
 * A handler class for the ExtendedEntity wrapper class.
 *
 * @author CountryGamer
 */
object ExtendedEntityHandler {

	/**
	 * Holds data in respect for the IExtendedEntityProperties.
	 * The key is the IExtendedEntityProperties class (ExtendedEntity wrapper class).
	 * The value it returns holds 2 string objects. The first (index 0) is the class key which
	 * is what IExtendedEntityProperties uses track it's classes. The second (index 1) is a boolean
	 * in string object format declaring whether this class has data that should persist past death.
	 */
	private val extendedProperties: util.HashMap[Class[_ <: ExtendedEntity], Array[String]] = new
					util.HashMap[Class[_ <: ExtendedEntity], Array[String]]

	/**
	 * Register an ExtendedEntity with a class key and a notice saying whether or not there is data
	 * that persists past death.
	 * @param classKey
	 * @param extendedClass
	 * @param persistPastDeath
	 */
	def registerExtended(classKey: String, extendedClass: Class[_ <: ExtendedEntity],
			persistPastDeath: Boolean): Unit = {
		ExtendedEntityHandler.extendedProperties
				.put(extendedClass, Array[String](classKey, (persistPastDeath + "")))
	}

	/**
	 * Get the map of ExtendedEntities and data regarding the class usage
	 * @return
	 */
	def getExtendedProperties: util.HashMap[Class[_ <: ExtendedEntity], Array[String]] = {
		return ExtendedEntityHandler.extendedProperties
	}

	/**
	 * Get IExtendedEntityProperties from a player instance and an ExtendedEntity class.
	 * @param player
	 * @param extendedClass
	 * @return
	 * null if no mapping
	 */
	final def getExtended(player: EntityPlayer,
			extendedClass: Class[_ <: ExtendedEntity]): IExtendedEntityProperties = {
		if (ExtendedEntityHandler.extendedProperties.containsKey(extendedClass)) {
			return player
					.getExtendedProperties(
			            ExtendedEntityHandler.extendedProperties.get(extendedClass)(0))
		}
		else {
			System.out.println(
				"ERROR: No ExtendedEntity class with the name of " + extendedClass.getSimpleName +
						" registered.")
			return null
		}
	}

	/**
	 * Register an EntityPlayer instance with an ExtendedEntity class
	 * @param player
	 * @param extendedClass
	 * @return
	 * true if registration was completed, false if otherwise
	 */
	final def registerPlayer(player: EntityPlayer,
			extendedClass: Class[_ <: ExtendedEntity]): Boolean = {
		var ent: ExtendedEntity = null
		try {
			ent = extendedClass.getConstructor(classOf[EntityPlayer]).newInstance(player)
		}
		catch {
			case e: IllegalArgumentException => {
				e.printStackTrace
			}
			case e: SecurityException => {
				e.printStackTrace
			}
			case e: InstantiationException => {
				e.printStackTrace
			}
			case e: IllegalAccessException => {
				e.printStackTrace
			}
			case e: InvocationTargetException => {
				e.printStackTrace
			}
			case e: NoSuchMethodException => {
				e.printStackTrace
			}
		}
		if (ent != null) {
			player.registerExtendedProperties(
				ExtendedEntityHandler.extendedProperties.get(extendedClass)(0), ent)
			return true
		}
		return false
	}

	def syncEntity(extendedPlayer: ExtendedEntity) {
		extendedPlayer.syncEntity
	}

}