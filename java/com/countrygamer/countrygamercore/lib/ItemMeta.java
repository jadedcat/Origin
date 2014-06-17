package com.countrygamer.countrygamercore.lib;

import net.minecraft.block.Block;
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
	private boolean isBlock = false;
	
	ItemMeta() {
	}
	
	public ItemMeta(Block block, int metadata) {
		this(Item.getItemFromBlock(block), metadata);
		this.isBlock = true;
	}
	
	public ItemMeta(Item item, int metadata) {
		this.item = item;
		this.meta = metadata;
	}
	
	public void saveToNBT(NBTTagCompound compound) {
		compound.setInteger("item_id", Item.getIdFromItem(this.item));
		compound.setInteger("metadata", this.meta);
		compound.setBoolean("isBlock", this.isBlock);
		
	}
	
	public void loadFromNBT(NBTTagCompound compound) {
		this.item = Item.getItemById(compound.getInteger("item_id"));
		this.meta = compound.getInteger("metadata");
		this.isBlock = compound.getBoolean("isBlock");
		
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.equals(obj, false);
	}
	
	public boolean equals(Object obj, boolean ignoreMeta) {
		if (obj instanceof ItemMeta) {
			ItemMeta that = (ItemMeta) obj;
			return this.item.equals(that.item) && (ignoreMeta ? true : this.meta == that.meta);
		}
		return false;
	}
	
	public ItemMeta copy() {
		if (this.isBlock)
			return new ItemMeta(Block.getBlockFromItem(this.item), this.meta);
		return new ItemMeta(this.item, this.meta);
	}
	
	public ItemStack getItemStack(int size) {
		return new ItemStack(this.item, size < 1 ? 1 : (size > 64 ? 64 : size), this.meta);
	}
	
	public void print() {
		System.out.println(this.item.getUnlocalizedName() + " - " + this.meta);
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public Block getBlock() {
		if (this.isBlock) {
			return Block.getBlockFromItem(this.item);
		}
		return null;
	}
	
	public int getMetadata() {
		return this.meta;
	}
	
}
