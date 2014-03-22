package com.countrygamer.core.blocks.tile;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.CraftingSystem.DiagramRecipes;

/**
 * Created by Country_Gamer on 3/18/14.
 */
public class TileEntityDiagram extends TileEntityInventoryBase {

	private DiagramRecipes.Recipe recipe;
	public boolean finishedCrafting = false;

	public TileEntityDiagram () {
		super("Diagram", 0, 1);
	}

	public DiagramRecipes.Recipe getRecipe () {
		return recipe;
	}

	public boolean setRecipe(String blockName) {
		if (DiagramRecipes.recipeList.containsKey(blockName)) {
			this.recipe = DiagramRecipes.recipeList.get(blockName);
			this.checkInventorySizeWithRecipe(this.recipe);
			return true;
		}
		return false;
	}

	private void checkInventorySizeWithRecipe(DiagramRecipes.Recipe recipe) {
		int recipeSize = recipe.getNeededSizeOfInventory();
		if (this.inv.length != recipeSize) {
			ItemStack[] currentInv = new ItemStack[this.inv.length];
			for (int i = 0; i < this.inv.length; i++) currentInv[i] = this.inv[i];
			this.inv = new ItemStack[recipeSize];
			for (int i = 0; i < recipeSize; i++) {
				if (i < currentInv.length) {
					this.inv[i] = currentInv[i];
				}
			}

		}

	}

	@Override
	public void updateEntity() {

	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		super.setInventorySlotContents(slot, itemStack);

		boolean finished = true;
		for (int i = 0; i < this.inv.length; i++) {
			if (this.getStackInSlot(i) == null) {
				finished = false;
				break;
			}
		}

		if (finished && this.recipe != null) {
			this.finishedCrafting = true;
			this.getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord,
					this.recipe.outputBlock, this.recipe.blockMetadata, 3);
			if (this.getWorldObj().getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this)
				this.getWorldObj().setTileEntity(this.xCoord, this.yCoord, this.zCoord, null);
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setBoolean("finishedCrafting", this.finishedCrafting);
		if (this.recipe != null) {
			NBTTagCompound recipeTagCom = new NBTTagCompound();
			this.recipe.writeToNBT(recipeTagCom);
			tagCom.setTag("recipe", recipeTagCom);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.finishedCrafting = tagCom.getBoolean("finishedCrafting");
		NBTTagCompound recipeTagCom = tagCom.getCompoundTag("recipe");
		this.recipe = new DiagramRecipes.Recipe();
		this.recipe.readFromNBT(recipeTagCom);
	}

	// Client Server Sync
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCom = new NBTTagCompound();
		this.writeToNBT(tagCom);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
				this.blockMetadata, tagCom);
	}

	public ArrayList<float[]>[] getUVsToRender() {
		ArrayList<float[]>[] uvsToRender = new ArrayList[6];
		if (this.getRecipe() != null) {
			for (int side = 0; side < 6; side++) {
				uvsToRender[side] = new ArrayList<float[]>();
				for (DiagramRecipes.Recipe.RecipeComponent component
						: this.getRecipe().getComponents()) {
					if (component.hasUVForSide(side)) {
						uvsToRender[side].addAll(component.getUVsForSide(side));
					}
				}
			}
		}
		return uvsToRender;
	}



}
