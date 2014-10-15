package com.countrygamer.cgo.library.client

import com.countrygamer.cgo.library.client.render.BlockCamouflageRender
import com.countrygamer.cgo.library.common.nethandler.PacketHandler
import com.countrygamer.cgo.library.common.network.PacketSyncExtendedProperties
import com.countrygamer.cgo.library.common.{CommonProxy, Origin}
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
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

	override def getClientElement(ID: Int, player: EntityPlayer, world: World, coord: Vec3,
			tileEntity: TileEntity): AnyRef = {
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
