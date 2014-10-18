package com.temportalist.origin.wrapper.common.generic

import java.util

import net.minecraft.util.RegistryNamespaced

/**
 *
 *
 * @author CountryGamer
 */
class GenericRegister() {

	private final val DATA: RegistryNamespaced = new RegistryNamespaced
	private val keys: util.ArrayList[String] = new util.ArrayList[String]

	def registerObjects(): Unit = {}

	def registerObject(id: Int, key: String, obj: AnyRef) {
		this.DATA.addObject(id, key, obj)
		this.keys.add(key)
	}

	def getObject(key: String): AnyRef = {
		this.DATA.getObject(key)
	}

	def getObject(id: Int): AnyRef = {
		this.DATA.getObjectById(id)
	}

	def getRegister: RegistryNamespaced = {
		this.DATA
	}

	def getKeys: Array[String] = {
		val retKeys: Array[String] = new Array[String](this.keys.size)

		var i = 0
		for (i <- 0 to this.keys.size) {
			retKeys(i) = this.keys.get(i)
		}

		retKeys
	}

}
