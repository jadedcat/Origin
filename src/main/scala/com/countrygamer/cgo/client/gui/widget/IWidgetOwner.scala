package com.countrygamer.cgo.client.gui.widget

/**
 *
 *
 * @author CountryGamer
 */
trait IWidgetOwner {

	def getXSize(): Int

	def getYSize(): Int

	def getGuiLeft(): Int

	def getGuiTop(): Int

}
