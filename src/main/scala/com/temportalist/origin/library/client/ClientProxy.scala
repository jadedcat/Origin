package com.temportalist.origin.library.client

import java.util

import com.temportalist.origin.library.client.gui.GuiRadialMenuHandler
import com.temportalist.origin.library.client.gui.config.GuiConfig
import com.temportalist.origin.library.client.render.BlockCamouflageRender
import com.temportalist.origin.library.common.helpers.RegisterHelper
import com.temportalist.origin.library.common.nethandler.PacketHandler
import com.temportalist.origin.library.common.network.PacketSyncExtendedProperties
import com.temportalist.origin.library.common.{CommonProxy, Origin}
import cpw.mods.fml.client.IModGuiFactory
import cpw.mods.fml.client.IModGuiFactory.{RuntimeOptionCategoryElement, RuntimeOptionGuiHandler}
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class ClientProxy() extends CommonProxy with IModGuiFactory {

	override def registerRender(): Unit = {
		RenderingRegistry.registerBlockHandler(BlockCamouflageRender)

		RegisterHelper.registerHandler(GuiRadialMenuHandler, null)

	}

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, tileEntity: TileEntity): AnyRef = {
		null
	}

	override def syncPacket(message: PacketSyncExtendedProperties, player: EntityPlayer) {
		if (FMLCommonHandler.instance.getEffectiveSide.isClient) {
			PacketHandler.sendToServer(Origin.pluginID, message)
		}
		else {
			super.syncPacket(message, player)
		}
	}

	override def addArmor(armor: String): Int = {
		RenderingRegistry.addNewArmourRendererPrefix(armor)
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
