package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.vec.{V3O, V3O$}
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{Vec3, EnumFacing}
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
object WorldHelper {

	def isClient(): Boolean = {
		FMLCommonHandler.instance().getEffectiveSide.isClient
	}

	def isServer(): Boolean = {
		FMLCommonHandler.instance().getEffectiveSide.isServer
	}

	def getWorld(dim: Int): World = {
		if (this.isServer()) DimensionManager.getWorld(dim)
		else this.getWorld_client()
	}

	@SideOnly(Side.CLIENT)
	def getWorld_client(): World = Minecraft.getMinecraft.theWorld

	def getBlock(world: World, x: Int, y: Int, z: Int, dir: EnumFacing): Block = {
		V3O.from(x, y, z, dir).getBlock(world)
	}

	def getBlockState(world: World, x: Int, y: Int, z: Int, dir: EnumFacing): IBlockState = {
		V3O.from(x, y, z, dir).getBlockState(world)
	}

	def getTileEntity(world: World, x: Int, y: Int, z: Int, dir: EnumFacing): TileEntity = {
		V3O.from(x, y, z, dir).getTile(world)
	}

	def isInFieldOfView(viewer: Entity, viewee: Entity): Boolean = {
		val entityLookVec: V3O = new V3O(viewer.getLook(1.0F)) //.normalize()
		val differenceVec: V3O = new V3O(
				viewee.posX - viewer.posX,
				viewee.posY + viewee.height.asInstanceOf[Double] -
						(viewer.posY + viewer.getEyeHeight().asInstanceOf[Double]),
				viewee.posZ - viewer.posZ
			)

		val lengthVec: Double = differenceVec.toVec3().lengthVector()

		val differenceVec_normal: Vec3 = differenceVec.toVec3().normalize()

		val d1: Double = entityLookVec.toVec3().dotProduct(differenceVec_normal)

		if (d1 > (1.0D - 0.025D) / lengthVec && WorldHelper.canEntityBeSeen(viewer, viewee)) {
			true
		}
		else {
			false
		}
	}

	def canEntityBeSeen(viewer: Entity, viewee: Entity): Boolean = {
		viewee.getEntityWorld.rayTraceBlocks(
			new Vec3(
				viewee.posX, viewee.posY + viewee.getEyeHeight.asInstanceOf[Double], viewee.posZ)
			,
			new Vec3(
				viewer.posX, viewer.posY + viewer.getEyeHeight.asInstanceOf[Double], viewer.posZ
			)
		) == null
	}

}
