package com.temportalist.origin.foundation.client

import com.temportalist.origin.api.common.register.Registry
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.{KeyInputEvent, MouseInputEvent}
import cpw.mods.fml.common.{Loader, Optional}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import modwarriors.notenoughkeys.api.KeyBindingPressedEvent
import net.minecraft.client.settings.KeyBinding

import scala.collection.mutable.ListBuffer

/**
 *
 *
 * @author  TheTemportalist  5/20/15
 */
@SideOnly(Side.CLIENT)
object KeyHandler {

	Registry.registerHandler(this)

	private val binders = new ListBuffer[IKeyBinder]()

	def register(binder: IKeyBinder): Unit = {
		this.binders += binder
	}

	def isNEKLoaded: Boolean = Loader.isModLoaded("notenoughkeys")

	@SubscribeEvent
	def onMouseEvent(event: MouseInputEvent): Unit = {
		if (!this.isNEKLoaded) this.checkKeys()
	}

	@SubscribeEvent
	def onKeyEvent(event: KeyInputEvent): Unit = {
		if (!this.isNEKLoaded) this.checkKeys()
	}

	@Optional.Method(modid = "notenoughkeys")
	@SubscribeEvent
	def onNEKEvent(event: KeyBindingPressedEvent): Unit = {
		this.onKeyPressed(event.keyBinding)
	}

	private def checkKeys(): Unit = {
		this.binders.foreach(binder => binder.checkKeys())
	}

	private def onKeyPressed(binding: KeyBinding): Unit = {
		this.binders.foreach(binder => binder.onKeyPressed(binding))
	}

}
