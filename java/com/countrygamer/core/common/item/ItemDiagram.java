package com.countrygamer.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.core.Base.common.item.ItemBase;
import com.countrygamer.core.common.Core_Depreciated;
import com.countrygamer.core.common.tileentity.TileEntityDiagram;

/**
 * Created by Country_Gamer on 3/18/14.
 */
public class ItemDiagram extends ItemBase {

	private final Block block;

	public ItemDiagram (String modid, String name) {
		super(modid, name);
		this.block = Core_Depreciated.diagramBlock;
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		this.generateTagCompound(itemStack);
	}

	private void generateTagCompound(ItemStack itemStack) {
		NBTTagCompound tagCom = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();

		tagCom.setString("diagram_recipe_key", "IotaTable");

		itemStack.setTagCompound(tagCom);
	}

	@Override
	public boolean onItemUse(
			ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side,
			float x1, float y1, float z1) {
		if (itemStack != null && !itemStack.hasTagCompound())
			this.generateTagCompound(itemStack);


		Block block = world.getBlock(x, y, z);

		if (block == Blocks.snow && (world.getBlockMetadata(x, y, z) & 7) < 1) {
			side = 1;
		}
		else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush
				&& (block == null || !block.isReplaceable(world, x, y, z))) {
			if (side == 0) {
				--y;
			}

			if (side == 1) {
				++y;
			}

			if (side == 2) {
				--z;
			}

			if (side == 3) {
				++z;
			}

			if (side == 4) {
				--x;
			}

			if (side == 5) {
				++x;
			}
		}

		if (itemStack.stackSize == 0) {
			return false;
		}
		else if (!player.canPlayerEdit(x, y, z, side, itemStack)) {
			return false;
		}
		else if (y == 255 && this.block.getMaterial().isSolid()) {
			return false;
		}
		else {
			if (world.canPlaceEntityOnSide(this.block, x, y, z, false, side, player, itemStack)) {
				block = this.block;
				int j1 = this.getMetadata(itemStack.getItemDamage());
				int k1 = this.block.onBlockPlaced(world, x, y, z, side, x1, y1, z1, j1);

				if (placeBlockAt(itemStack, player, world, x, y, z, side, x1, y1, z1, k1)) {
					world.playSoundEffect(
							(double)((float)x + 0.5F),
							(double)((float)y + 0.5F),
							(double)((float)z + 0.5F),
							block.stepSound.func_150496_b(),
							(block.stepSound.getVolume() + 1.0F) / 2.0F,
							block.stepSound.getPitch() * 0.8F);

					TileEntity tEnt = world.getTileEntity(x, y, z);
					if (tEnt != null && tEnt instanceof TileEntityDiagram) {
						TileEntityDiagram tileEnt = (TileEntityDiagram) tEnt;

						String text = "";
						text += itemStack != null ? "Stack" : "No Stack";
						if (itemStack != null) {
							text += "|";
							text += itemStack.hasTagCompound() ? "TagCom" : "No TagCom";
							if (itemStack.hasTagCompound()) {
								text += "|";
								text += itemStack.getTagCompound().getString("diagram_recipe_key");
							}
						}
						//if (!world.isRemote) Core.log.info(text);


						if (itemStack != null && itemStack.hasTagCompound()) {
							String diagramName = itemStack.getTagCompound().getString("diagram_recipe_key");
							if (!diagramName.equals("")) {
								((TileEntityDiagram) tEnt).setRecipe(diagramName);
							}

						}
					}
					else Core_Depreciated.log.info(tEnt == null ? "Null tile ent" : "Not TileEntityDiagram");

					--itemStack.stackSize;
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack The item stack that was used to place the block. This can be changed inside the method.
	 * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param side The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(
			ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata) {
		if (!world.setBlock(x, y, z, this.block, metadata, 3)) {
			return false;
		}

		if (world.getBlock(x, y, z) == this.block) {
			this.block.onBlockPlacedBy(world, x, y, z, player, stack);
			this.block.onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true;
	}

}
