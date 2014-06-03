package com.countrygamer.countrygamercore.Base.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.countrygamercore.Base.common.tileentity.TileEntityInventoryBase;

/**
 * Base class for all Containers
 * 
 * @author Country_Gamer
 * 
 */
public class ContainerBase extends Container {
	
	/**
	 * The player which opened this container
	 */
	public final EntityPlayer player;
	
	/**
	 * The inventory to which this container belongs
	 */
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
	
	/**
	 * Get the inventory to which this container belongs
	 * 
	 * @return
	 */
	public IInventory getIInventory() {
		return this.inventory;
	}
	
	/**
	 * Discern whether or not the inventory that this container belongs to is a tile entity
	 * 
	 * @return
	 */
	public boolean isAttachedToTileEntity() {
		return this.inventory instanceof TileEntityInventoryBase;
	}
	
	/**
	 * Get the tile entity to which this container belongs
	 * 
	 * @return
	 */
	public TileEntityInventoryBase getTileEntity() {
		if (this.isAttachedToTileEntity()) return (TileEntityInventoryBase) this.inventory;
		return null;
	}
	
	/**
	 * Discern whether or not the inventory that this container belongs to is an Item
	 * 
	 * @return
	 */
	public boolean isAttachedToItem() {
		return this.inventory instanceof InventoryItemBase;
	}
	
	/**
	 * Get the item inventory to which this container belongs
	 * 
	 * @return
	 */
	public InventoryItemBase getItemInventory() {
		if (this.isAttachedToItem()) return (InventoryItemBase) this.inventory;
		return null;
	}
	
	/**
	 * Used to register slots for this container
	 * Subclasses SHOULD use this method (that is the reason we have containers),
	 * however, subclasses do not NEED to use this method.
	 */
	protected void registerSlots() {
	}
	
	/**
	 * Method to auto-generate slots connected to this player's inventory
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
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
	
	/**
	 * Discern whether or not the passed player can use this container.
	 */
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		if (this.isAttachedToTileEntity())
			return ((TileEntityInventoryBase) this.inventory).isUseableByPlayer(this.player);
		else if (this.isAttachedToItem()) return true;
		return false;
	}
	
	/**
	 * Used to move item stacks about when a player shiftclicks
	 */
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
