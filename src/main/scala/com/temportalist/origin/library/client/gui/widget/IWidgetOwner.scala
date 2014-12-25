package com.temportalist.origin.library.client.gui.widget

import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
trait IWidgetOwner {

	def getXSize: Int

	def getYSize: Int

	def getGuiLeft: Int

	def getGuiTop: Int

}
