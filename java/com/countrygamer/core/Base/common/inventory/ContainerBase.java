package com.countrygamer.core.Base.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class ContainerBase extends Container {
	
	public final EntityPlayer player;
	private final IInventory inventory;
	
	/**
	 * Used by items to save NBT data
	 */
	public boolean needsUpdate;
	
	public ContainerBase(EntityPlayer player, IInventory inventory) {
		this.player = player;
		this.inventory = inventory;
		
		this.registerSlots();
	}
	
	public IInventory getIInventory() {
		return this.inventory;
	}
	
	public boolean isAttachedToTileEntity() {
		return this.inventory instanceof TileEntityInventoryBase;
	}
	
	public TileEntityInventoryBase getTileEntity() {
		if (this.isAttachedToTileEntity()) return (TileEntityInventoryBase) this.inventory;
		return null;
	}
	
	public boolean isAttachedToItem() {
		return this.inventory instanceof InventoryItemBase;
	}
	
	public InventoryItemBase getItemInventory() {
		if (this.isAttachedToItem()) return (InventoryItemBase) this.inventory;
		return null;
	}
	
	protected void registerSlots() {
	}
	
	protected void registerPlayerSlots(int offsetX, int offsetY) {
		int i;
		// PLAYER INVENTORY - uses default locations for standard inventory
		// texture file
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9, (8 + j * 18)
						+ offsetX, (84 + i * 18) + offsetY));
			}
		}
		
		// PLAYER ACTION BAR - uses default locations for standard action bar
		// texture file
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(this.player.inventory, i, (8 + i * 18) + offsetX,
					142 + offsetY));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		if (this.isAttachedToTileEntity())
			return ((TileEntityInventoryBase) this.inventory).isUseableByPlayer(this.player);
		else if (this.isAttachedToItem()) return true;
		return false;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotiD) {
		return null;
	}
	
	@Override
	public ItemStack slotClick(int slotID, int buttonPressed, int flag, EntityPlayer player) {
		if (this.isAttachedToItem()) this.needsUpdate = true;
		return super.slotClick(slotID, buttonPressed, flag, player);
	}
	
	public void writeToNBT() {
		if (this.isAttachedToItem()) {
			ItemStack heldStack = this.player.getHeldItem();
			if (heldStack != null) {
				NBTTagCompound tagCom = heldStack.getTagCompound();
				if (tagCom == null) tagCom = new NBTTagCompound();
				
				((InventoryItemBase) this.inventory).writeToNBT(tagCom);
				
				this.player.getHeldItem().setTagCompound(tagCom);
			}
		}
	}
	
}
