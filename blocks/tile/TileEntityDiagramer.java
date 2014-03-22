package com.countrygamer.core.blocks.tile;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Core;
import com.countrygamer.core.item.ItemDiagram;
import com.countrygamer.core.item.ItemMoldedClay;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by dustinyost on 3/21/14.
 */
public class TileEntityDiagramer extends TileEntityInventoryBase {

	public TileEntityDiagramer () {
		super("Diagramer", 4, 64);
	}

	@Override
	public boolean isItemValidForSlot (int i, ItemStack itemstack) {
		ItemStack currentStackInSlot = this.getStackInSlot(i);
		int currentSize = 0;
		if (currentStackInSlot != null) currentSize = currentStackInSlot.stackSize;
		switch (i) {
			case 0:
				return itemstack.getItem() instanceof ItemDiagram &&
						(currentSize + itemstack.stackSize < itemstack.getMaxStackSize());
			case 1:
				return itemstack.getItem() ==
						Item.getItemFromBlock(Blocks.cobblestone) &&
						(currentSize + itemstack.stackSize < itemstack.getMaxStackSize());
			case 2:
				return itemstack.getItem() == Items.clay_ball &&
						(currentSize + itemstack.stackSize < itemstack.getMaxStackSize());
			default:
				return false;
		}
	}

	@Override
	public void updateEntity() {
		ItemStack currentMold = this.getStackInSlot(0) != null ? this.getStackInSlot(0).copy() : null;
		ItemStack cobbleStack = this.getStackInSlot(1) != null ? this.getStackInSlot(1).copy() : null;
		ItemStack clayStack = this.getStackInSlot(2) != null ? this.getStackInSlot(2).copy() : null;
		ItemStack newMold = this.getStackInSlot(3) != null ? this.getStackInSlot(3).copy() : null;
		if (newMold == null && cobbleStack != null && clayStack != null) {
			--cobbleStack.stackSize;
			--clayStack.stackSize;
			if (cobbleStack.stackSize <= 0) cobbleStack = null;
			if (clayStack.stackSize <= 0) clayStack = null;
			this.setInventorySlotContents(1, cobbleStack);
			this.setInventorySlotContents(2, clayStack);

			newMold = new ItemStack(Core.diagram);
			String recipeKey = "IotaTable";
			if (currentMold != null) {
				NBTTagCompound currentMoldTag =
						currentMold.hasTagCompound() ? currentMold.getTagCompound() : new NBTTagCompound();
				recipeKey = currentMoldTag.getString(ItemMoldedClay.recipieNameKey);
			}
			NBTTagCompound newMoldStack = new NBTTagCompound();
			newMoldStack.setString(ItemMoldedClay.recipieNameKey, recipeKey);
			newMold.setTagCompound(newMoldStack);

			if (currentMold != null) {
				newMold.stackSize += 1;
				this.setInventorySlotContents(0, null);
			}
			this.setInventorySlotContents(3, newMold);
		}



	}


}
