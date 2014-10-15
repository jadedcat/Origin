package com.countrygamer.cgo.wrapper.common

import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
import net.minecraft.world.World

/**
 *
 *
 * @author CountryGamer
 */
trait ProxyWrapper extends IGuiHandler {

	def registerRender(): Unit

	override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int): AnyRef = {
		this.getClientElement(
			ID, player, world, Vec3.createVectorHelper(x, y, x), world.getTileEntity(x, y, z)
		)
	}

	override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int): AnyRef = {
		this.getServerElement(
			ID, player, world, Vec3.createVectorHelper(x, y, x), world.getTileEntity(x, y, z)
		)
	}

	def getClientElement(ID: Int, player: EntityPlayer, world: World,
<<<<<<< HEAD
			x: Int, y: Int, z: Int, tileEntity: TileEntity): AnyRef

	def getServerElement(ID: Int, player: EntityPlayer, world: World,
			x: Int, y: Int, z: Int, tileEntity: TileEntity): AnyRef
=======
			coord: Vec3, tileEntity: TileEntity): AnyRef

	def getServerElement(ID: Int, player: EntityPlayer, world: World,
			coord: Vec3, tileEntity: TileEntity): AnyRef
>>>>>>> 4614287... Much updates. Many things have been moved and re-organized.

}
