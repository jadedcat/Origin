package com.countrygamer.countrygamercore.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemMeta {
	
	public static ItemMeta getFromStack(ItemStack itemStack) {
		if (itemStack != null) {
			return new ItemMeta(itemStack.getItem(), itemStack.getItemDamage());
		}
		return null;
	}
	
	public static ItemMeta getFromNBT(NBTTagCompound compound) {
		ItemMeta im = new ItemMeta();
		im.loadFromNBT(compound);
		return im;
	}
	
	private Item item;
	private int meta;
	
	ItemMeta() {
	}
	
	public ItemMeta(Item item, int metadata) {
		this.item = item;
		this.meta = metadata;
	}
	
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("item_id", Item.getIdFromItem(this.item));
		compound.setInteger("metadata", this.meta);
		
	}
	
	public void loadFromNBT(NBTTagCompound compound) {
		this.item = Item.getItemById(compound.getInteger("item_id"));
		this.meta = compound.getInteger("metadata");
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.equals(obj, false);
	}
	
	public boolean equals(Object obj, boolean ignoreMeta) {
		if (obj instanceof ItemMeta) {
			ItemMeta that = (ItemMeta) obj;
			return this.item.equals(that.item)
					&& (ignoreMeta ? true : this.meta == that.meta);
		}
		return false;
	}
	
	public ItemMeta copy() {
		return new ItemMeta(this.item, this.meta);
	}
	
}
