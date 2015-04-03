package com.temportalist.origin.library.common.utility

import java.util
import java.util.Random

import com.temportalist.origin.library.common.lib.BlockState
import com.temportalist.origin.library.common.lib.vec.{V3O, BlockPos}
import net.minecraft.block.Block
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.{Item, ItemStack}
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

	def spawnItemStack(world: World, pos: V3O, itemStack: ItemStack, random: Random,
			delay: Int): Unit = {
		if (itemStack != null) {
			val entityitem: EntityItem = new EntityItem(world,
				pos.x + random.nextFloat * 0.8F + 0.1F,
				pos.y + random.nextFloat * 0.8F + 0.1F,
				pos.z + random.nextFloat * 0.8F + 0.1F,
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

	def spawnItemStack(world: World, pos: V3O, state: BlockState, random: Random,
			delay: Int): Unit = {
		this.spawnItemStack(world, pos, States.getStack(state), random, delay)
	}

	@Deprecated
	def areStacksMatching(a: ItemStack, b: ItemStack): Boolean =
		this.areStacksMatching(a, b, checkSize = false, checkNBT = true)

	@Deprecated
	def areStacksMatching(a: ItemStack, b: ItemStack, checkSize: Boolean,
			checkNBT: Boolean): Boolean = {
		a.getItem == b.getItem && a.getItemDamage == b.getItemDamage &&
				(!checkSize || a.stackSize == b.stackSize) &&
				(!checkNBT || ItemStack.areItemStackTagsEqual(a, b))
	}

	def createStack(obj: AnyRef, data: Map[String, Any]): ItemStack =
		this.createStack(obj, 1, 0, data)

	def createStack(obj: AnyRef, size: Int, meta: Int, data: Map[String, Any]): ItemStack = {
		val stack: ItemStack = obj match {
			case b: Block => new ItemStack(b, size, meta)
			case i: Item => new ItemStack(i, size, meta)
			case _ => throw new IllegalArgumentException(
				"Illegal argument " + obj.toString + " of class " + obj.getClass.getCanonicalName)
		}
		if (data != null) {
			val nbt: NBTTagCompound = new NBTTagCompound
			for ((key: String, value: Any) <- data) {
				value match {
					case i: Int => nbt.setInteger(key, i)
					case stack: ItemStack =>
						val stackTag: NBTTagCompound = new NBTTagCompound
						stack.writeToNBT(stackTag)
						nbt.setTag(key, stackTag)
					case _ =>
				}
			}
			stack.setTagCompound(nbt)
		}
		stack
	}

	def doStacksMatch(a: ItemStack, b: ItemStack,
			meta: Boolean, size: Boolean, nbt: Boolean, nil: Boolean): Boolean = {
		if ((a == null && b != null) || (b == null && a != null)) nil
		else if (a != null && b != null) {
			a.getItem == b.getItem &&
					(!meta || a.getItemDamage == b.getItemDamage) &&
					(!size || a.stackSize == b.stackSize) &&
					(!nbt || ItemStack.areItemStackTagsEqual(a, b))
		}
		else true
	}

	def canFit(a: ItemStack, b: ItemStack): Boolean = this.canFit(a, b, a.stackSize)

	def canFit(a: ItemStack, b: ItemStack, amt: Int): Boolean = {
		a != null && this.doStacksMatch(a, b, meta = true, size = false, nbt = true, nil = true) &&
				(b == null || b.stackSize + amt <= b.getMaxStackSize)
	}

}
