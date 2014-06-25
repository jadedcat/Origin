package com.countrygamer.countrygamercore.common.inventory;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class GhostCamouflageSlot extends GhostSlot {
	
	public GhostCamouflageSlot(IInventory inv, int slotID, int x, int y) {
		super(inv, slotID, x, y);
	}
	
	public GhostCamouflageSlot(IInventory inv, int slotID, int x, int y, int maxSize) {
		super(inv, slotID, x, y, maxSize);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		Block block = Block.getBlockFromItem(itemStack.getItem());
		return block != Blocks.air && (block.getRenderType() == 0);
	}
	
}
