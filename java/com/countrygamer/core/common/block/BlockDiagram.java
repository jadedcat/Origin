package com.countrygamer.core.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.core.Base.common.block.BlockContainerBase;
import com.countrygamer.core.common.Core_Depreciated;
import com.countrygamer.core.common.craftingsystem.DiagramRecipes;
import com.countrygamer.core.common.tileentity.TileEntityDiagram;
import com.countrygamer.countrygamercore.common.Core;

/**
 * Created by Country_Gamer on 3/18/14.
 */
public class BlockDiagram extends BlockContainerBase {

	public BlockDiagram (Material mat, String modid, String name, Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDiagram();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public Item getItem (World world, int x, int y, int z) {
		return Core_Depreciated.diagram;
	}

	@Override
	public Item getItemDropped (int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Core_Depreciated.diagram;
	}

	@Override
	public void breakBlock(
			World world, int x, int y, int z, Block par5, int par6) {
		TileEntityDiagram tileEnt = (TileEntityDiagram) world.getTileEntity(x, y, z);
		Random rand = new Random();

		if (tileEnt != null && !tileEnt.finishedCrafting) {
			for (int j1 = 0; j1 < tileEnt.getSizeInventory(); j1++) {
				ItemStack itemstack = tileEnt.getStackInSlot(j1);
				if (itemstack != null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					entityitem = new EntityItem(world,
							(double) ((float) x + f),
							(double) ((float) y + f1),
							(double) ((float) z + f2), itemstack.copy());
					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) rand.nextGaussian()
							* f3 + 0.2F);
					entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}
					world.spawnEntityInWorld(entityitem);
				}
			}
			world.func_147453_f(x, y, z, par5);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean onBlockActivated(
			World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		String textToLog = "\n";
		ItemStack itemStack = player.getHeldItem();

		if (itemStack == null) {
			TileEntityDiagram tileEnt = (TileEntityDiagram)world.getTileEntity(x, y, z);
			int sum = 0;
			for (int i = 0; i < tileEnt.getSizeInventory(); i++) {
				if (tileEnt.getStackInSlot(i) != null) {
					sum++;
				}
			}
			String mcSide = world.isRemote ? "Client" : "Server";
			textToLog += mcSide + ": " + sum;
			Core_Depreciated.log.info(textToLog);
			return false;
		}

		// add text to log regarding the current stack
		textToLog +=
				itemStack.getDisplayName()
				+ "; " + itemStack.getItemDamage()
				+ "\n\n";

		// Get the tile entity
		TileEntityDiagram tileEnt = (TileEntityDiagram)world.getTileEntity(x, y, z);
		// get the recipe from the tile entity
		DiagramRecipes.Recipe recipe = tileEnt.getRecipe();
		// if recipe not null
		if (recipe != null) {
			// Get every RecipeComponent of the recipe
			for (DiagramRecipes.Recipe.RecipeComponent component : recipe.getComponents()) {
				// add text to the log detailing the current component iteration
				textToLog +=
						component.name
						+ "; " + component.inputStack.getDisplayName()
						+ "; " + component.inputStack.getItemDamage()
						+ "\n";

				// Check to make sure the currently held itemstack matches the one needed
				if (itemStack.getItem() == component.inputStack.getItem() &&
						itemStack.getItemDamage() == component.inputStack.getItemDamage() &&
						ItemStack.areItemStackTagsEqual(itemStack, component.inputStack)) {
					// Add the side to the output text log
					textToLog += "Side: " + side + "\n";

					// get the boolean to check if the slot in the tile entity is empty
					boolean emptySlot = tileEnt.getStackInSlot(component.slotiD) == null;
					// Add check to log
					textToLog += emptySlot ? "Empty Slot" : "Full Slot";
					textToLog += "\n";

					// actually run the check
					if (emptySlot) {
						// Get a list of all valid UV coords for RecipeComponent
						ArrayList<float[]> rUVs = component.getUVsForSide(side);

						// Get the current UV coords
						float[] uv = this.getUVFromSideAndOffset(side, x1, y1, z1);
						float u = uv[0], v = uv[1];

						// if the retrieved UVs are not null
						if (rUVs != null) {
							// get every uv set from the retrieved RecipeComponent UVs
							for (float[] rUV : rUVs) {
								// Assign UV variables from uv set retrieved
								float minU = rUV[0];
								float maxU = rUV[1];
								float minV = rUV[2];
								float maxV = rUV[3];

								// Check if the current uv values are valid for this set
								boolean validU = minU <= u && u <= maxU;
								boolean validV = minV <= v && v <= maxV;

								// Add uv values to text log
								String uvText = "\n";
								uvText += minU + " < " + u + "\n";
								uvText += u + " < " + maxU + "\n";
								uvText += minV + " < " + v + "\n";
								uvText += v + " < " + maxV + "\n";
								textToLog += uvText + "\n";

								// Run the if statement to check if the values are valid
								if (validU && validV) {
									// add text to log saying the it is all valid
									textToLog += "Valid uv and itemstack" + "\n";
									// copy the current stack
									ItemStack inputStack = itemStack.copy();
									// set the stack size of the insert stack to 1
									inputStack.stackSize = 1;
									// set the inventory slot of this component to the isertting stack
									tileEnt.setInventorySlotContents(component.slotiD, inputStack);

									// decrease the return stack's stacksize
									--itemStack.stackSize;
									// return the stack
									player.setCurrentItemOrArmor(0, itemStack);

									// log before returning
									if (!world.isRemote) Core_Depreciated.log.info(textToLog);
									return true;

								}
								else {
									// add text to the log
									textToLog += "Invalid UV" + "\n";
								}
							}
						}
					}
				}
				// add spacer to the log
				textToLog += "\n\n";
			}
		}
		// log before returning
		if (!world.isRemote) Core_Depreciated.log.info(textToLog);
		return false;
	}

	private float[] getUVFromSideAndOffset(int side, float x1, float y1, float z1) {
		float u = 0F, v = 0F;
		if (side == 1 || side == 0) {
			// top or bpttom
			u = x1;
			v = z1;
		}
		else if (side == 2 || side == 3) {
			// north or south
			u = x1;
			v = y1;
		}
		else if (side == 5 || side == 4) {
			// east or west
			u = z1;
			v = y1;
		}

		return new float[] {u, v};
	}


}
