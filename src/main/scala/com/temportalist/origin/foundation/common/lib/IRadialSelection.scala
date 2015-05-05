package com.temportalist.origin.foundation.common.lib

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

/**
 *
 *
 * @author TheTemportalist
 */
trait IRadialSelection {

	@SideOnly(Side.CLIENT)
	def draw(mc: Minecraft, x: Double, y: Double, z: Double, w: Double, h: Double): Unit

	def triggerSelection(player: EntityPlayer): Unit

}
