package com.countrygamer.countrygamercore.Base.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.countrygamer.countrygamercore.Base.Plugin.PluginBase;
import com.countrygamer.countrygamercore.Base.common.inventory.ContainerBase;

public class ItemInvBase extends ItemBase {
	
	public static final String inventoryDataKey = "invData";
	public static final String basicDataKey = "generalData";
	
	private final PluginBase pluginInstance;
	private final int guiID;
	
	public ItemInvBase(String modid, String name) {
		this(modid, name, null, 0);
	}
	
	public ItemInvBase(String modid, String name, PluginBase instance, int guiID) {
		super(modid, name);
		this.setMaxStackSize(1);
		
		this.pluginInstance = instance;
		this.guiID = guiID;
		
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 1;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		if (!itemstack.hasTagCompound()) {
			NBTTagCompound tagCom = new NBTTagCompound();
			tagCom.setTag(ItemInvBase.basicDataKey, new NBTTagCompound());
			tagCom.setTag(ItemInvBase.inventoryDataKey, new NBTTagCompound());
			itemstack.setTagCompound(tagCom);
		}
		if (this.pluginInstance != null) {
			if (!world.isRemote && entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if (player.openContainer != null && player.openContainer instanceof ContainerBase
						&& ((ContainerBase) player.openContainer).needsUpdate) {
					((ContainerBase) player.openContainer).writeToNBT();
					((ContainerBase) player.openContainer).needsUpdate = false;
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (this.pluginInstance != null) {
			player.openGui(this.pluginInstance, this.guiID, world, (int) player.posX,
					(int) player.posY, (int) player.posZ);
		}
		return itemStack;
	}
	
}
