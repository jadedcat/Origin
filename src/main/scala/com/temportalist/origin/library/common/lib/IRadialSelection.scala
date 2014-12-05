package com.temportalist.origin.library.common.lib

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft

/**
 *
 *
 * @author TheTemportalist
 */
trait IRadialSelection {

	@SideOnly(Side.CLIENT)
	def draw(mc: Minecraft, x: Double, y: Double, z: Double, w: Double, h: Double): Unit

}