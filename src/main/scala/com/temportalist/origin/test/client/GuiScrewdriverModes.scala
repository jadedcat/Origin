package com.temportalist.origin.test.client

import com.temportalist.origin.library.client.gui.{GuiRadialMenuHandler, GuiRadialMenu}
import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.handlers.RegisterHelper
import com.temportalist.origin.library.common.lib.IRadialSelection
import com.temportalist.origin.library.common.nethandler.PacketHandler
import com.temportalist.origin.test.{ScrewdriverMode, PacketUpdateMode, Sonic}
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.{MouseInputEvent, KeyInputEvent}
import org.lwjgl.input.Keyboard

/**
 *
 *
 * @author TheTemportalist
 */
class GuiScrewdriverModes(stack: ItemStack)
		extends GuiRadialMenu(40, 100, ScrewdriverMode.getModes(stack, forced = true)) {

	override def shouldSelect(): Boolean =
		!Keyboard.isKeyDown(GuiScrewdriverModes.openRadial.getKeyCode)

	override def onSelectionOf(index: Int, item: IRadialSelection): Unit = {
		// todo do this inheirently, calling IRadialSelection.onSelection
		//println("Selected: " + item.asInstanceOf[ScrewdriverMode].getName())
		PacketHandler.sendToServerAndClients(Origin.MODID, new PacketUpdateMode(
			item.asInstanceOf[ScrewdriverMode]
		))
	}

}

object GuiScrewdriverModes {

	val openRadial: KeyBinding = new KeyBinding(
		"key.openRadial", Keyboard.KEY_K, "key.categories.gameplay"
	)

	def register(): Unit = {
		RegisterHelper.registerHandler(this)
		ClientRegistry.registerKeyBinding(this.openRadial)
	}

	@SubscribeEvent
	def keyPress(event: KeyInputEvent): Unit = {
		this.checkPressed()
	}

	@SubscribeEvent
	def mousePress(event: MouseInputEvent): Unit = {
		this.checkPressed()
	}

	def checkPressed(): Unit = {
		if (Keyboard.isKeyDown(GuiScrewdriverModes.openRadial.getKeyCode) &&
				Rendering.mc.currentScreen == null) {
			val stack: ItemStack = Rendering.mc.thePlayer.getCurrentEquippedItem
			if (stack.getItem == Sonic.screwdriver)
				GuiRadialMenuHandler.display(new GuiScrewdriverModes(stack))
		}
	}

}