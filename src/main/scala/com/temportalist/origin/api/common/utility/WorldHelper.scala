package com.temportalist.origin.api.common.utility

import com.temportalist.origin.api.common.lib.V3O
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.common.util.ForgeDirection

/**
 *
 *
 * @author TheTemportalist
 */
object WorldHelper {

	def getSide: Side = FMLCommonHandler.instance().getEffectiveSide

	def isClient: Boolean = this.getSide.isClient

	def isServer: Boolean = this.getSide.isServer

	def isClient(ent: Entity): Boolean = ent.worldObj.isRemote

	def isServer(ent: Entity): Boolean = !this.isClient(ent)

	def getWorld(dim: Int): World = {
		if (this.isServer) DimensionManager.getWorld(dim)
		else this.getWorld_client
	}

	def isOverworld(world: World): Boolean = world.provider.dimensionId == 0

	@SideOnly(Side.CLIENT)
	def getWorld_client: World = Minecraft.getMinecraft.theWorld

	def isBlock(item: Item): Boolean = Block.getBlockFromItem(item) != null

	def getBlock(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection): Block = {
		V3O.from(x, y, z, dir).getBlock(world)
	}

	def getTileEntity(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection): TileEntity = {
		V3O.from(x, y, z, dir).getTile(world)
	}

	def isInFieldOfView(viewer: Entity, viewee: Entity): Boolean = {
		val look: Vec3 = viewer.getLookVec
		if (look == null) return false
		val entityLookVec: V3O = new V3O(look) //.normalize()
		val differenceVec: V3O = new V3O(
				viewee.posX - viewer.posX,
				viewee.posY + viewee.height.asInstanceOf[Double] -
						(viewer.posY + viewer.getEyeHeight().asInstanceOf[Double]),
				viewee.posZ - viewer.posZ
			)

		val lengthVec: Double = differenceVec.toVec3.lengthVector()

		val differenceVec_normal: Vec3 = differenceVec.toVec3.normalize()

		val d1: Double = entityLookVec.toVec3.dotProduct(differenceVec_normal)

		if (d1 > (1.0D - 0.025D) / lengthVec && WorldHelper.canEntityBeSeen(viewer, viewee)) {
			true
		}
		else {
			false
		}
	}

	def canEntityBeSeen(viewer: Entity, viewee: Entity): Boolean = {
		viewee.worldObj.rayTraceBlocks(
			Vec3.createVectorHelper(
				viewee.posX, viewee.posY + viewee.getEyeHeight.asInstanceOf[Double], viewee.posZ)
			,
			Vec3.createVectorHelper(
				viewer.posX, viewer.posY + viewer.getEyeHeight.asInstanceOf[Double], viewer.posZ
			)
		) == null
	}

}
