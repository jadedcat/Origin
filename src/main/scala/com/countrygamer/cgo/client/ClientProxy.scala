package com.countrygamer.cgo.client

import com.countrygamer.cgo.client.render.BlockCamouflageRender
import com.countrygamer.cgo.common.network.{PacketHandler, PacketSyncExtendedProperties}
import com.countrygamer.cgo.common.{CommonProxy, Origin}
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
class ClientProxy() extends CommonProxy {

	override def registerRender(): Unit = {
		RenderingRegistry.registerBlockHandler(BlockCamouflageRender)

	}

	override def getClientGuiElement(p1: Int, p2: EntityPlayer, p3: World, p4: Int, p5: Int,
			p6: Int): AnyRef = {
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

}
