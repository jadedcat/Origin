package com.temportalist.origin.library.client.gui

import com.temportalist.origin.library.client.utility.GuiHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Mouse

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
object GuiRadialMenuHandler {

	val mc: Minecraft = Minecraft.getMinecraft
	var selectCurrent: Boolean = false

	def getRadialMenu(): GuiRadialMenu = {
		if (this.mc.currentScreen != null && this.mc.currentScreen.isInstanceOf[GuiRadialMenu])
			this.mc.currentScreen.asInstanceOf[GuiRadialMenu]
		else null
	}

	def display(menu: GuiRadialMenu): Unit = {
		GuiHelper.display(menu)

		// this stuff grabs the cursor so it doesnt render in the radial menu
		this.mc.inGameHasFocus = true
		this.mc.mouseHelper.grabMouseCursor()

	}

	@SubscribeEvent
	def overlayEvent(event: RenderGameOverlayEvent): Unit = {
		if (event.`type` == RenderGameOverlayEvent.ElementType.CROSSHAIRS &&
				this.getRadialMenu() != null)
			event.setCanceled(true)
	}

	@SubscribeEvent
	def onClientTick(event: TickEvent.ClientTickEvent): Unit = {
		val menu: GuiRadialMenu = this.getRadialMenu()
		if (menu != null && event.phase == TickEvent.Phase.START) {
			if (menu.shouldSelect()) {
				menu.selectCurrent()
			}
		}
	}

	@SubscribeEvent
	def onRenderTick(event: TickEvent.RenderTickEvent): Unit = {
		val menu: GuiRadialMenu = this.getRadialMenu()
		if (menu != null) {
			if (event.phase == TickEvent.Phase.START) {
				Mouse.getDX
				Mouse.getDY
				this.mc.mouseHelper.deltaX = 0
				this.mc.mouseHelper.deltaY = 0
			}
			else if (!this.mc.gameSettings.hideGUI && !this.mc.isGamePaused) {
				menu.renderMenu()
			}
		}
	}

}
