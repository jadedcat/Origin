package com.temportalist.origin.library.common.utility

import java.util
import java.util.Random

import com.temportalist.origin.library.common.lib.BlockState
import com.temportalist.origin.library.common.lib.vec.BlockPos
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
object Stacks {

	def spawnDrops(world: World, pos: BlockPos, drops: util.List[ItemStack]): Unit = {
		if (!drops.isEmpty) {
			val random: Random = new Random
			var drop: ItemStack = null
			for (i <- 0 until drops.size()) {
				drop = drops.get(i)
				if (drop != null && drop.getItem != null) {
					Stacks.spawnItemStack(world, pos, drop, random, 10)
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
			entityitem.delayBeforeCanPickup = delay
			if (itemStack.hasTagCompound) {
				entityitem.getEntityItem
						.setTagCompound(itemStack.getTagCompound.copy.asInstanceOf[NBTTagCompound])
			}
			if (!world.isRemote) world.spawnEntityInWorld(entityitem)
		}
	}

	def spawnItemStack(world: World, pos: BlockPos, state: BlockState, random: Random,
			delay: Int): Unit = {
		this.spawnItemStack(world, pos, States.getStack(state), random, delay)
	}

	def areStacksMatching(a: ItemStack, b: ItemStack): Boolean =
		this.areStacksMatching(a, b, checkSize = false, checkNBT = true)

	def areStacksMatching(a: ItemStack, b: ItemStack, checkSize: Boolean,
			checkNBT: Boolean): Boolean = {
		a.getItem == b.getItem && a.getMetadata == b.getMetadata &&
				(!checkSize || a.stackSize == b.stackSize) &&
				(!checkNBT || ItemStack.areItemStackTagsEqual(a, b))
	}

}
