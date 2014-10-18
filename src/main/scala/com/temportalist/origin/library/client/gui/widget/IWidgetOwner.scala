package com.temportalist.origin.library.client.gui.widget

import cpw.mods.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
trait IWidgetOwner {

	def getXSize: Int

	def getYSize: Int

	def getGuiLeft: Int

	def getGuiTop: Int

}
