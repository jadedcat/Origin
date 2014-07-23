package com.countrygamer.cgo.wrapper.common

import cpw.mods.fml.common.network.IGuiHandler

/**
 *
 *
 * @author CountryGamer
 */
trait ProxyWrapper extends IGuiHandler {

	def registerRender(): Unit

}
