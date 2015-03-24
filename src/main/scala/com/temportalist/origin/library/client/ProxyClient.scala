package com.temportalist.origin.library.client

import java.util

import scala.collection.mutable.ListBuffer

import com.temportalist.origin.api.rendering.ISpriteMapper
import com.temportalist.origin.library.client.gui.config.GuiConfig
import com.temportalist.origin.library.client.gui.{GuiRadialMenuHandler, HealthOverlay}
import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.handlers.RegisterHelper
import com.temportalist.origin.library.common.lib.LogHelper
import com.temportalist.origin.library.common.nethandler.PacketHandler
import com.temportalist.origin.library.common.network.PacketSyncExtendedProperties
import com.temportalist.origin.library.common.utility.ItemRenderingHelper
import com.temportalist.origin.library.common.{CGOOptions, Origin, ProxyCommon}
import com.temportalist.origin.test.Sonic
import com.temportalist.origin.test.client.{GuiDataCore, GuiScrewdriverModes}
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.SoundCategory
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.client.event.{TextureStitchEvent, ModelBakeEvent}
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.{RuntimeOptionCategoryElement, RuntimeOptionGuiHandler}
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 *
 *
 * @author TheTemportalist
 */
class ProxyClient() extends ProxyCommon with IModGuiFactory {

	override def registerRender(): Unit = {
		ItemRenderingHelper.registerItemRenders()
		RegisterHelper.registerHandler(GuiRadialMenuHandler, HealthOverlay)

		ModelBakery.addVariantName(Sonic.screwdriver,
			"origin:screwdriver0", "origin:screwdriver1", "origin:screwdriver2"
		)
		GuiScrewdriverModes.register()

	}

	override def postInit(): Unit = {
		CGOOptions.volumeControls.foreach({ case (name: String, volume: Float) =>
			Rendering.mc.gameSettings.setSoundLevel(SoundCategory.getCategory(name), volume)
		})
	}

	val spritees: ListBuffer[ISpriteMapper] = new ListBuffer[ISpriteMapper]

	override def registerSpritee(spritee: ISpriteMapper): Unit = {
		spritees += spritee
	}

	@SubscribeEvent
	def bake(event: ModelBakeEvent): Unit = {
		ItemRenderingHelper.bake(event.modelRegistry)
	}

	@SubscribeEvent
	def pre_Sprites(event: TextureStitchEvent.Pre): Unit = {
		for (spritee: ISpriteMapper <- this.spritees) {
			LogHelper.info(Origin.MODNAME, "Loading sprite for " + spritee.getResourceLocation().toString)
			event.map.registerSprite(spritee.getResourceLocation())
		}
	}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		if (ID == 0) {
			//println("Open DataCore")
			return new GuiDataCore(player)
		}
		null
	}

	override def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		if (FMLCommonHandler.instance.getEffectiveSide.isClient) {
			PacketHandler.sendToServer(Origin.MODID, message)
		}
		else {
			super.syncPacket(message, player)
		}
	}

	override def addArmor(armor: String): Int = {
		// todo find a way to bind armor rendering
		//RenderingRegistry.addNewArmourRendererPrefix(armor)
		0
	}

	override def initialize(minecraftInstance: Minecraft): Unit = {}

	override def runtimeGuiCategories(): util.Set[RuntimeOptionCategoryElement] = {
		null
	}

	override def getHandlerFor(element: RuntimeOptionCategoryElement): RuntimeOptionGuiHandler = {
		null
	}

	override def mainConfigGuiClass(): Class[_ <: GuiScreen] = {
		classOf[GuiConfig]
	}
}
