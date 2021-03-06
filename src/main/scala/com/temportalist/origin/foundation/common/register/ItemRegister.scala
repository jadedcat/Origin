package com.temportalist.origin.foundation.common.register

import com.temportalist.origin.api.common.register.Register

/**
 *
 *
 * @author TheTemportalist
 */
trait ItemRegister extends Register {

	override final def priority: Int = 0

	override final def getRegFuncType: Class[_ <: Register] = classOf[ItemRegister]

	def registerItemsPostBlock(): Unit = {}

	def registerCrafting(): Unit = {}

	def registerSmelting(): Unit = {}

	def registerOther(): Unit = {}

}
