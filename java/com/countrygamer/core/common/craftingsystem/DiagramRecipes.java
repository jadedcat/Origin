package com.countrygamer.core.common.craftingsystem;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.common.Core_Depreciated;

/**
 * Created by Country_Gamer on 3/18/14.
 */
public class DiagramRecipes {

	public static HashMap<String, Recipe> recipeList = new HashMap<String, Recipe>();

	public static void addRecipe(String name, Recipe recipe) {
		recipeList.put(name, recipe);
	}


	public static class Recipe {

		public static class RecipeComponent {

			public String name;
			public int slotiD = 0;
			//public Block usageBlock;
			//public int blockMetadata;
			public ItemStack inputStack;

			// index = side
			// 6 indecies
			// each index is an array of float[]s
			ArrayList<float[]>[] sidedUVs = new ArrayList[6];

			public RecipeComponent() {}

			public RecipeComponent(String name, Recipe recipe, ItemStack itemStack) {
				this.name = name;
				this.slotiD = recipe.getComponents().size();
				//this.usageBlock = block;
				//this.blockMetadata = blockMetadata;
				this.inputStack = itemStack;
				for (int i = 0; i < this.sidedUVs.length; i++) {
					//this.sidedUVs[i] = new ArrayList<float[]>();
				}
			}

			public RecipeComponent generateFullForAllSides() {
				this.generateForAllSides(0.0F, 1.0F, 0.0F, 1.0F);
				return this;
			}

			public RecipeComponent generateForAllSides(float minU, float maxU, float minV, float maxV) {
				for (int i = 0; i < 6; i++) { // generate for each side
					this.addSidedUVArea(i, minU, maxU, minV, maxV);
				}
				return this;
			}

			public RecipeComponent addSidedUVArea(int side, float minU, float maxU, float minV, float maxV) {
				if (side < 0 || side >= 6) side = 0;
				if (this.sidedUVs[side] == null) this.sidedUVs[side] = new ArrayList<float[]>();
				this.sidedUVs[side].add(new float[] {minU, maxU, minV, maxV});
				return this;
			}

			public boolean hasUVForSide(int side) {
				if (this.sidedUVs == null || this.sidedUVs[side] == null) return false;
				return (!(side < 0 || side >= 6)) && !this.sidedUVs[side].isEmpty();
			}

			public ArrayList<float[]> getUVsForSide(int side) {
				return (side < 0 || side >= 6) ? null : this.sidedUVs[side];
			}

			public void writeToNBT(NBTTagCompound tagCom) {
				NBTTagCompound sidesTagCom = new NBTTagCompound();
				for (int side = 0; side < 6; side++) {
					NBTTagCompound sideTagCom = new NBTTagCompound();
					if (this.hasUVForSide(side)) {
						ArrayList<float[]> uvList = this.getUVsForSide(side);
						NBTTagCompound uvs = new NBTTagCompound();
						for (int i = 0; i < uvList.size(); i++) {
							float[] uv = uvList.get(i);
							NBTTagCompound uvTagCom = new NBTTagCompound();
							for (int j = 0; j < uv.length; j++) {
								uvTagCom.setFloat(j + "", uv[j]);
							}
							uvs.setTag(i + "", uvTagCom);
						}
						uvs.setInteger("uvList_size", uvList.size());
						sideTagCom.setTag("uvs", uvs);
					}
					sidesTagCom.setTag(side + "", sideTagCom);
				}
				tagCom.setString("name", this.name);
				tagCom.setInteger("slotid", this.slotiD);

				NBTTagCompound stackTagCom = new NBTTagCompound();
				this.inputStack.writeToNBT(stackTagCom);
				tagCom.setTag("stackTag", stackTagCom);

				tagCom.setTag("sides", sidesTagCom);
			}

			public void readFromNBT(NBTTagCompound tagCom) {
				this.name = tagCom.getString("name");
				this.slotiD = tagCom.getInteger("slotid");

				NBTTagCompound stackTagCom = tagCom.getCompoundTag("stackTag");
				this.inputStack = new ItemStack(Blocks.air);
				this.inputStack.readFromNBT(stackTagCom);

				this.sidedUVs = new ArrayList[6];

				NBTTagCompound sidesTagCom = tagCom.getCompoundTag("sides");
				for (int side = 0; side < 6; side++) {
					this.sidedUVs[side] = new ArrayList<float[]>();

					NBTTagCompound sideTagCom = sidesTagCom.getCompoundTag(side + "");

					NBTTagCompound uvs = sidesTagCom.getCompoundTag("uvs");
					for (int i = 0; i < uvs.getInteger("uvList_size"); i++) {
						NBTTagCompound uvTagCom = uvs.getCompoundTag(i + "");
						float[] uvSet = new float[4];
						for (int j = 0; j < uvSet.length; j++) {
							float coor = uvTagCom.getFloat(j + "");
							uvSet[j] = coor;
						}
						this.sidedUVs[side].set(i, uvSet);
					}

				}
			}

		}

		public Block outputBlock;
		public int blockMetadata;

		ArrayList<RecipeComponent> components = new ArrayList<RecipeComponent>();

		public Recipe() {}

		public Recipe(Block outputBlock, int blockMetadata) {
			this.outputBlock = outputBlock;
			this.blockMetadata = blockMetadata;
		}

		public void addRecipeComponent(RecipeComponent component) {
			this.components.add(component);
		}

		public int getNeededSizeOfInventory() {
			return this.components.size();
		}

		public ArrayList<RecipeComponent> getComponents() {
			return this.components;
		}

		public RecipeComponent getComponentFromSidedUV(int side, float u, float v) {
			for (RecipeComponent comp : this.components) {
				if (comp.hasUVForSide(side)) {
					ArrayList<float[]> uvList = comp.getUVsForSide(side);

					for (float[] uvs : uvList) {
						float minU = uvs[0];
						float maxU = uvs[1];
						float minV = uvs[2];
						float maxV = uvs[3];

						if ((minU <= u && u <= maxU) && (minV <= v && v <= maxV)) {
							return comp;
						}
					}
				}
			}
			return null;
		}

		public void printComponents() {
			String output = "\n";
			for (RecipeComponent component : this.components) {
				for (int side = 0; side < 6; side++) {
					if (component.sidedUVs[side] != null && component.hasUVForSide(side)) {
						ArrayList<float[]> uvList = component.getUVsForSide(side);
						for (float[] uvs : uvList) {
							output += "\t";
							output += "Side: " + side;
							output += "\n\t";
							output += component.inputStack.getDisplayName() + ", " + component.inputStack.getItemDamage();
							output += "\n\t\t";
							output += "iU: " + uvs[0] + "F, ";
							output += "aU: " + uvs[1] + "F, ";
							output += "iV: " + uvs[2] + "F, ";
							output += "aV: " + uvs[3] + "F\n";
						}
						output += "\n";
					}
				}
			}
			if (output.equals("\n"))
				output = "There are no components for the recipe of "
						+ this.outputBlock.getUnlocalizedName()
						+ " with metadata of "
						+ this.blockMetadata;
			Core_Depreciated.log.info(output);
		}

		public void writeToNBT(NBTTagCompound tagCom) {
			tagCom.setInteger("block_id", Block.getIdFromBlock(this.outputBlock));
			tagCom.setInteger("block_meta", this.blockMetadata);

			NBTTagCompound compsTagCom = new NBTTagCompound();
			int compIndex = 0;
			for (RecipeComponent rc : this.components) {
				NBTTagCompound componentTagCom = new NBTTagCompound();
				rc.writeToNBT(componentTagCom);
				compsTagCom.setTag(compIndex + "", componentTagCom);
				compIndex++;
			}
			compsTagCom.setInteger("components", compIndex);

			tagCom.setTag("recipe_components", compsTagCom);

		}

		public void readFromNBT(NBTTagCompound tagCom) {
			this.outputBlock = Block.getBlockById(tagCom.getInteger("block_id"));
			this.blockMetadata = tagCom.getInteger("block_meta");

			NBTTagCompound compsTagCom = tagCom.getCompoundTag("recipe_components");
			int compSize = compsTagCom.getInteger("components");

			this.components.clear();
			for (int i = 0; i < compSize; i++) {
				NBTTagCompound componentTagCom = compsTagCom.getCompoundTag(i + "");
				RecipeComponent rc = new RecipeComponent();
				rc.readFromNBT(componentTagCom);
				this.components.add(rc);
			}

		}

	}


}
