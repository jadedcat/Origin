package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.vec.Vector3O
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
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
		Vector3O.from(x, y, z, dir).getBlock(world)
	}

	def getBlockState(world: World, x: Int, y: Int, z: Int, dir: EnumFacing): IBlockState = {
		Vector3O.from(x, y, z, dir).getBlockState(world)
	}

	def getTileEntity(world: World, x: Int, y: Int, z: Int, dir: EnumFacing): TileEntity = {
		Vector3O.from(x, y, z, dir).getTile(world)
	}

}
