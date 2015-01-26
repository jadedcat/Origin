package com.temportalist.origin.library.common.utility

import java.util
import java.util.Random

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
object Drops {

	def spawnDrops(world: World, pos: BlockPos, drops: util.List[ItemStack]): Unit = {
		if (!drops.isEmpty) {
			val random: Random = new Random
			var drop: ItemStack = null
			for (i <- 0 until drops.size()) {
				drop = drops.get(i)
				if (drop != null && drop.getItem != null) {
					Drops.spawnItemStack(world, pos, drop, random, 10)
				}
			}
		}
	}

	def spawnItemStack(world: World, pos: BlockPos, itemStack: ItemStack, random: Random,
			delay: Int): Unit = {
		if (itemStack != null) {
			val f: Float = random.nextFloat * 0.8F + 0.1F
			val f1: Float = random.nextFloat * 0.8F + 0.1F
			val f2: Float = random.nextFloat * 0.8F + 0.1F
			var entityitem: EntityItem = null
			entityitem = new EntityItem(world,
				(pos.getX.asInstanceOf[Float] + f).asInstanceOf[Double],
				(pos.getY.asInstanceOf[Float] + f1).asInstanceOf[Double],
				(pos.getZ.asInstanceOf[Float] + f2).asInstanceOf[Double],
				itemStack.copy
			)
			val f3: Float = 0.05F
			entityitem.motionX = (random.nextGaussian.asInstanceOf[Float] * f3).asInstanceOf[Double]
			entityitem.motionY = (random.nextGaussian.asInstanceOf[Float] * f3 + 0.2F)
					.asInstanceOf[Double]
			entityitem.motionZ = (random.nextGaussian.asInstanceOf[Float] * f3).asInstanceOf[Double]
			entityitem.setPickupDelay(delay)
			if (itemStack.hasTagCompound) {
				entityitem.getEntityItem
						.setTagCompound(itemStack.getTagCompound.copy.asInstanceOf[NBTTagCompound])
			}
			if (!world.isRemote) world.spawnEntityInWorld(entityitem)
		}
	}

	def spawnItemStack(world: World, pos: BlockPos, state: IBlockState, random: Random,
			delay: Int): Unit = {
		this.spawnItemStack(world, pos, States.getItemStackFromState(state), random, delay)
	}

}
