package com.countrygamer.cgo.common.lib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class UtilDrops {

	public static void spawnDrops(World world, double x, double y, double z,
			ArrayList<ItemStack> drops) {
		if (!drops.isEmpty()) {
			Random random = new Random();
			for (ItemStack drop : drops) {
				if (drop != null && drop.getItem() != null) {
					UtilDrops.spawnItemStack(world, x, y, z, drop, random);
				}
			}
		}
	}

	public static void spawnItemStack(World world, double x, double y, double z,
			ItemStack itemStack, Random random) {
		if (itemStack != null) {
			float f = random.nextFloat() * 0.8F + 0.1F;
			float f1 = random.nextFloat() * 0.8F + 0.1F;
			float f2 = random.nextFloat() * 0.8F + 0.1F;
			EntityItem entityitem;

			entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1),
					(double) ((float) z + f2), itemStack.copy());
			float f3 = 0.05F;
			entityitem.motionX = (double) ((float) random.nextGaussian() * f3);
			entityitem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
			entityitem.motionZ = (double) ((float) random.nextGaussian() * f3);

			if (itemStack.hasTagCompound()) {
				entityitem.getEntityItem().setTagCompound(
						(NBTTagCompound) itemStack.getTagCompound().copy());
			}
			if (!world.isRemote)
				world.spawnEntityInWorld(entityitem);
		}
	}

}
