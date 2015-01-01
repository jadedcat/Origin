package com.temportalist.origin.library.common.register

/**
 *
 *
 * @author TheTemportalist
 */
trait ItemRegister extends Register {

	def registerRenders_Init(): Unit

	def registerItemsPostBlock(): Unit = {}

	def registerCrafting(): Unit = {}

	def registerSmelting(): Unit = {}

	def registerOther(): Unit = {}

}
