package com.countrygamer.countrygamercore.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import com.countrygamer.core.Base.Plugin.registry.PluginOptionRegistry;
import com.countrygamer.countrygamercore.lib.CoreUtil;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CoreOptions implements PluginOptionRegistry {
	
	// Craft Smelt
	public static boolean netherStar;
	public static boolean lead;
	public static boolean string;
	public static boolean smeltFlesh;
	public static boolean blazeRod;
	public static boolean cobbleGravel, gravelSand, gsDirt;
	public static boolean ghastTear;
	public static boolean netherrack, soulSand, netherBrick;
	public static boolean emerald;
	public static boolean spiderEye, rottenFlesh, fermSpiderEye;
	public static boolean sapling;
	public static boolean carrot, potato;
	public static boolean saddle;
	
	@Override
	public boolean hasCustomConfiguration() {
		return false;
	}
	
	@Override
	public void customizeConfiguration(FMLPreInitializationEvent event) {
	}
	
	@Override
	public void registerOptions(Configuration config) {
		String csCate = "Crafting and Smelting";
		
		CoreOptions.netherStar = CoreUtil.getAndComment(config, csCate,
				"Nether Star", "Crafting a Nether Star", true);
		CoreOptions.lead = CoreUtil.getAndComment(config, csCate, "Lead",
				"Another recipe for the Lead", false);
		CoreOptions.string = CoreUtil.getAndComment(config, csCate, "String",
				"Wool to String recipe", true);
		CoreOptions.smeltFlesh = CoreUtil.getAndComment(config, csCate,
				"Smelt Flesh", "Smelt Rotten Flesh to Leather", true);
		CoreOptions.blazeRod = CoreUtil.getAndComment(config, csCate,
				"Craft Blaze Rods", "Craft Blaze Rods with Blaze Powder", true);
		CoreOptions.cobbleGravel = CoreUtil.getAndComment(config, csCate,
				"Cobble to Gravel", "Craft Cobblestone to Gravel", true);
		CoreOptions.gravelSand = CoreUtil.getAndComment(config, csCate,
				"Gravel to Sand", "Craft Gravel to Sand", true);
		CoreOptions.gsDirt = CoreUtil.getAndComment(config, csCate,
				"GravelSand to Dirt", "Craft Gravel and Sand into Dirt", true);
		CoreOptions.ghastTear = CoreUtil.getAndComment(config, csCate, "Ghast Tear",
				"Craft a Ghast Tear", true);
		CoreOptions.netherrack = CoreUtil.getAndComment(config, csCate,
				"Netherrack", "Craft NetherRack from Blaze Powder and Stone", true);
		CoreOptions.soulSand = CoreUtil.getAndComment(config, csCate, "Soulsand",
				"Craft Soulsand from Blaze Powder and Sand", true);
		CoreOptions.netherBrick = CoreUtil.getAndComment(config, csCate,
				"Nether Brick", "Craft Nether Bricks from Blaze Powder and Bricks",
				true);
		CoreOptions.emerald = CoreUtil.getAndComment(config, csCate, "Emerald",
				"Craft an Emerald", true);
		CoreOptions.spiderEye = CoreUtil.getAndComment(config, csCate, "Spider Eye",
				"Craft a Spider Eye", true);
		CoreOptions.rottenFlesh = CoreUtil.getAndComment(config, csCate,
				"Rotten Flesh", "Craft Rotton Flesh", true);
		CoreOptions.sapling = CoreUtil.getAndComment(config, csCate, "Saplings",
				"Craft Oak saplings", true);
		CoreOptions.carrot = CoreUtil.getAndComment(config, csCate, "Carrot",
				"Craft a carrot", true);
		CoreOptions.potato = CoreUtil.getAndComment(config, csCate, "Potato",
				"Craft a potato", true);
		CoreOptions.fermSpiderEye = CoreUtil.getAndComment(config, csCate,
				"Fermented Spider Eye", "Craft a Fermented Spider Eye", true);
		CoreOptions.saddle = CoreUtil.getAndComment(config, csCate, "Saddle",
				"Crafting Recipe for a vanilla Saddle", true);
		
	}
	
	public void vanillaCraftSmelt() {
		if (CoreOptions.netherStar) {
			GameRegistry.addRecipe(new ItemStack(Items.nether_star, 1), "xxx",
					"ccc", "vcv", 'x', new ItemStack(Items.skull, 1, 1), 'c',
					Blocks.soul_sand, 'v', Items.emerald);
		}
		if (CoreOptions.lead)
			GameRegistry.addRecipe(new ItemStack(Items.lead, 1), "  x", " x ",
					"x  ", 'x', Items.string);
		if (CoreOptions.string) {
			for (int i = 0; i < 16; i++) {
				GameRegistry.addShapelessRecipe(new ItemStack(Items.string, 4),
						new ItemStack(Blocks.wool, 1, i));
			}
		}
		if (CoreOptions.smeltFlesh)
			GameRegistry.addSmelting(Items.rotten_flesh, new ItemStack(
					Items.leather, 1), 50.0F);
		if (CoreOptions.blazeRod) {
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1), "x ", "x ",
					'x', Items.blaze_powder);
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1), " x", " x",
					'x', Items.blaze_powder);
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1), " x", "x ",
					'x', Items.blaze_powder);
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1), "x ", " x",
					'x', Items.blaze_powder);
		}
		if (CoreOptions.cobbleGravel)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.gravel, 2),
					Blocks.cobblestone);
		if (CoreOptions.gravelSand)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sand, 2),
					Blocks.gravel);
		if (CoreOptions.gsDirt)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dirt, 2),
					Blocks.gravel, Blocks.sand);
		if (CoreOptions.ghastTear)
			GameRegistry.addRecipe(new ItemStack(Items.ghast_tear, 1), " c ", "cxc",
					" c ", 'c', Items.blaze_powder, 'x', Items.water_bucket);
		if (CoreOptions.netherrack)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.netherrack, 1),
					Items.blaze_powder, Blocks.stone);
		if (CoreOptions.soulSand) {
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.soul_sand, 1),
					Items.blaze_powder, Blocks.sand);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.soul_sand, 1),
					Items.blaze_powder, Blocks.gravel);
		}
		if (CoreOptions.netherBrick) {
			GameRegistry.addRecipe(new ItemStack(Blocks.nether_brick, 1), " c ",
					"cxc", " c ", 'c', Items.blaze_powder, 'x', Blocks.stonebrick);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.netherbrick, 1),
					Items.blaze_powder, Items.brick);
		}
		if (CoreOptions.emerald)
			GameRegistry.addRecipe(new ItemStack(Items.emerald, 1), "cvc", "vbv",
					"cvc", 'c', (new ItemStack(Items.dye, 1, 10)), 'v',
					Items.diamond, 'b', Items.gold_ingot);
		if (CoreOptions.spiderEye)
			GameRegistry.addRecipe(new ItemStack(Items.spider_eye, 1), " v ", "cbc",
					" z ", 'c', (new ItemStack(Items.dye, 1, 1)), 'v',
					Items.rotten_flesh, 'b', Items.string, 'z', Items.blaze_powder);
		if (CoreOptions.rottenFlesh)
			GameRegistry.addRecipe(new ItemStack(Items.rotten_flesh, 1), "x", "v",
					'x', Items.sugar, 'v', Items.blaze_powder);
		if (CoreOptions.sapling)
			GameRegistry.addRecipe(new ItemStack(Blocks.sapling, 1, 0), "xxx",
					" c ", "vvv", 'x', Items.stick, 'v', Blocks.dirt, 'c',
					Blocks.log);
		if (CoreOptions.carrot)
			GameRegistry.addShapelessRecipe(new ItemStack(Items.carrot),
					Items.wheat_seeds, Blocks.leaves,
					new ItemStack(Items.dye, 1, 14));
		if (CoreOptions.potato)
			GameRegistry.addShapelessRecipe(new ItemStack(Items.potato),
					Items.wheat_seeds, Blocks.log);
		if (CoreOptions.fermSpiderEye)
			GameRegistry.addShapelessRecipe(
					new ItemStack(Items.fermented_spider_eye), Items.spider_eye,
					Items.sugar, Items.redstone);
		if (CoreOptions.saddle)
			GameRegistry.addRecipe(new ItemStack(Items.saddle), "lll", "s s", "i i",
					'l', Items.leather, 's', Items.string, 'i', Items.iron_ingot);
	}
	
}
