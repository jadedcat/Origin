package com.countrygamer.core.inventory;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;
import com.countrygamer.core.item.ItemDiagram;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by dustinyost on 3/21/14.
 */
public class ContainerDiagramer extends ContainerBlockBase {

	public ContainerDiagramer (InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}

	@Override
	protected boolean shiftClick(ItemStack itemStack, int slotiD) {
		return false;
	}

	@Override
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
		int slot = 0;
		int x = 140;
		int yBase = 10;
		this.addSlotToContainer(new Slot(this.tileEnt, slot++, x, yBase + 0) {
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				return this.getStack() == null &&
						itemStack.getItem() instanceof ItemDiagram;
			}
		});
		this.addSlotToContainer(new Slot(this.tileEnt, slot++, x, yBase + 20) {
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				return itemStack.getItem() == Item.getItemFromBlock(Blocks.cobblestone);
			}
		});
		this.addSlotToContainer(new Slot(this.tileEnt, slot++, x, yBase + 40) {
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				return itemStack.getItem() == Items.clay_ball;
			}
		});
		this.addSlotToContainer(new Slot(this.tileEnt, slot++, x, yBase + 60) {
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				return false;
			}
		});

		this.registerPlayerSlots(inventoryPlayer, 0, 0);
	}

}
