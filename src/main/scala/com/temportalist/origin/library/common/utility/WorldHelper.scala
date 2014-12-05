package com.temportalist.origin.library.common.utility

import com.temportalist.origin.library.common.lib.vec.Vector3b
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

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

	def getBlock(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection): Block = {
		Vector3b.from(x, y, z, dir).getBlock(world)
	}

	def getBlockMetadata(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection): Int = {
		Vector3b.from(x, y, z, dir).getBlockMetadata(world)
	}

	def getTileEntity(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection): TileEntity = {
		Vector3b.from(x, y, z, dir).getTileEntity(world)
	}

}
