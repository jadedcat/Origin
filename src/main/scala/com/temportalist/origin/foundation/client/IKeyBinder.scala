package com.temportalist.origin.foundation.client

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.{Keyboard, Mouse}

import scala.collection.mutable.ListBuffer

/**
 *
 *
 * @author  TheTemportalist  5/20/15
 */
@SideOnly(Side.CLIENT)
trait IKeyBinder {

	private val keys = new ListBuffer[KeyBinding]()

	protected def makeKeyBinding(unlocalizedName: String, key: Int,
			category: EnumKeyCategory): Unit = {
		this.makeKeyBinding(unlocalizedName, key, category.getName)
	}

	protected def makeKeyBinding(unlocalizedName: String, key: Int, category: String): Unit = {
		val binding = new KeyBinding(unlocalizedName, key, category)
		this.keys += binding
		ClientRegistry.registerKeyBinding(binding)
	}

	def checkKeys(): Unit = {
		this.keys.foreach(key => if (this.isKeyDown(key.getKeyCode)) this.onKeyPressed(key))
	}

	def isKeyDown(key: Int): Boolean = {
		if (key < 0) Mouse.isButtonDown(key + 100)
		else Keyboard.isKeyDown(key)
	}

	def onKeyPressed(keyBinding: KeyBinding): Unit

}
