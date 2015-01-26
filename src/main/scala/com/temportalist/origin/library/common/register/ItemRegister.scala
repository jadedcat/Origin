package com.temportalist.origin.library.common.register

/**
 *
 *
 * @author TheTemportalist
 */
trait ItemRegister extends Register {

	def registerItemsPostBlock(): Unit = {}

	def registerCrafting(): Unit = {}

	def registerSmelting(): Unit = {}

	def registerOther(): Unit = {}

}
