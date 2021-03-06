package com.temportalist.origin.api.client.gui

import java.util

import com.temportalist.origin.api.common.register.Registry
import com.temportalist.origin.api.common.utility.Scala
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.event.GuiScreenEvent

import scala.collection.mutable

/**
 *
 *
 * @author  TheTemportalist  5/7/15
 */
object GuiOverwrite {

	Registry.registerHandler(GuiOverwrite)

	@SideOnly(Side.CLIENT)
	private val guiOverwritingObjects: mutable.Map[Class[_ <: GuiScreen], util.List[GuiOverwriter]] =
		mutable.Map[Class[_ <: GuiScreen], util.List[GuiOverwriter]]()

	@SideOnly(Side.CLIENT)
	def registerOverwriter(obj: GuiOverwriter, classes: Class[_ <: GuiScreen]*): Unit = {
		classes.foreach(clazz => if (clazz != null) {
			if (!this.guiOverwritingObjects.contains(clazz))
				this.guiOverwritingObjects(clazz) = new util.ArrayList[GuiOverwriter]()
			this.guiOverwritingObjects(clazz).add(obj)
		})
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def guiInitPost(event: GuiScreenEvent.InitGuiEvent.Post): Unit = {
		this.iterateOverAllOverwriters(event.gui.getClass, (obj: GuiOverwriter) => {
			obj.overwriteGui(event.gui, event.buttonList)
		})
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def guiActionPre(event: GuiScreenEvent.ActionPerformedEvent.Pre): Unit = {
		this.iterateOverAllOverwriters(event.gui.getClass, (obj: GuiOverwriter) => {
			if (!obj.canClickButton(event.gui, event.button)) {
				event.setCanceled(true)
				return
			}
		})
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def guiActionPost(event: GuiScreenEvent.ActionPerformedEvent.Post): Unit = {
		this.iterateOverAllOverwriters(event.gui.getClass, (obj: GuiOverwriter) => {
			obj.onAction(event.gui, event.button)
		})
	}

	@SideOnly(Side.CLIENT)
	private def iterateOverAllOverwriters(guiClass: Class[_ <: GuiScreen],
			func: (GuiOverwriter) => Unit): Unit = {
		this.guiOverwritingObjects.foreach(f => if (f._1.isAssignableFrom(guiClass)) {
			Scala.iterateCol(f._2, func)
		})
	}

}
