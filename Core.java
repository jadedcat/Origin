package com.countrygamer.countrygamer_core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

import com.countrygamer.countrygamer_core.Handler.PacketPipeline;
import com.countrygamer.countrygamer_core.Handler.PacketTeleport;
import com.countrygamer.countrygamer_core.lib.CoreReference;
import com.countrygamer.countrygamer_core.lib.CoreUtil;
import com.countrygamer.countrygamer_core.proxy.ServerProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Mod class for the basic mod CountryGamer_Core
 * 
 * @author Country Gamer
 * 
 */
@Mod(modid = CoreReference.MOD_ID, name = CoreReference.MOD_NAME, version = CoreReference.MC_VERSION)
public class Core {

	public static final Logger log = Logger.getLogger(CoreReference.MOD_ID);
	@Instance("countrygamer_core")
	public static Core instance;
	@SidedProxy(clientSide = CoreReference.CLIENT_PROXY_CLASS, serverSide = CoreReference.SERVER_PROXY_CLASS)
	public static ServerProxy proxy;

	public static boolean DEBUG = true;

	public static HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
	public static HashMap<Integer, String> dimensions1 = new HashMap<Integer, String>();

	private static ArrayList<Item> tabItems = new ArrayList<Item>();
	private static ArrayList<Block> tabBlocks = new ArrayList<Block>();

	/** Craft Smelt */
	public static boolean netherStar;
	public static boolean lead;
	public static boolean string;
	public static boolean dirtCobble;
	public static boolean smeltFlesh;
	public static boolean blazeRod;
	public static boolean cobbleGravel, gravelSand, gsDirt;
	public static boolean ghastTear;
	public static boolean netherrack, soulSand, netherBrick;
	public static boolean emerald;
	public static boolean spiderEye, rottenFlesh, fermSpiderEye;
	public static boolean sapling;
	public static boolean carrot, potato;

	// Mods Loaded
	private static boolean neiLoaded = false;
	private static boolean morphLoaded = false;

	// Packet
	public static final PacketPipeline packetPipelineTeleport = new PacketPipeline();

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		// GameRegistry.registerPlayerTracker(new EventHandler());

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();

		this.config(event);

		proxy.registerRender();
		proxy.registerHandler();
	}

	public void config(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		String csCate = "Crafting and Smelting", general = config.CATEGORY_GENERAL;

		Core.netherStar = CoreUtil.getAndComment(config, csCate, "Nether Star",
				"Crafting a Nether Star", true);
		Core.lead = CoreUtil.getAndComment(config, csCate, "Lead",
				"Another recipe for the Lead", false);
		Core.string = CoreUtil.getAndComment(config, csCate, "String",
				"Wool to String recipe", true);
		Core.smeltFlesh = CoreUtil.getAndComment(config, csCate, "Smelt Flesh",
				"Smelt Rotten Flesh to Leather", true);
		Core.blazeRod = CoreUtil.getAndComment(config, csCate,
				"Craft Blaze Rods", "Craft Blaze Rods with Blaze Powder", true);
		Core.cobbleGravel = CoreUtil.getAndComment(config, csCate,
				"Cobble to Gravel", "Craft Cobblestone to Gravel", true);
		Core.gravelSand = CoreUtil.getAndComment(config, csCate,
				"Gravel to Sand", "Craft Gravel to Sand", true);
		Core.gsDirt = CoreUtil.getAndComment(config, csCate,
				"GravelSand to Dirt", "Craft Gravel and Sand into Dirt", true);
		Core.ghastTear = CoreUtil.getAndComment(config, csCate, "Ghast Tear",
				"Craft a Ghast Tear", true);
		Core.netherrack = CoreUtil.getAndComment(config, csCate, "Netherrack",
				"Craft NetherRack from Blaze Powder and Stone", true);
		Core.soulSand = CoreUtil.getAndComment(config, csCate, "Soulsand",
				"Craft Soulsand from Blaze Powder and Sand", true);
		Core.netherBrick = CoreUtil.getAndComment(config, csCate,
				"Nether Brick", "Craft Nether Bricks from Blaze Powder and Bricks", true);
		Core.emerald = CoreUtil.getAndComment(config, csCate, "Emerald", "Craft an Emerald",
				true);
		Core.spiderEye = CoreUtil.getAndComment(config, csCate, "Spider Eye",
				"Craft a Spider Eye", true);
		Core.rottenFlesh = CoreUtil.getAndComment(config, csCate,
				"Rotten Flesh", "Craft Rotton Flesh", true);
		Core.sapling = CoreUtil.getAndComment(config, csCate, "Saplings", "Craft Oak saplings",
				true);
		Core.carrot = CoreUtil
				.getAndComment(config, csCate, "Carrot", "Craft a carrot", true);
		Core.potato = CoreUtil
				.getAndComment(config, csCate, "Potato", "Craft a potato", true);
		Core.fermSpiderEye = CoreUtil.getAndComment(config, csCate,
				"Fermented Spider Eye", "Craft a Fermented Spider Eye", true);

		config.save();

		this.vanillaCraftSmelt();
	}

	public void vanillaCraftSmelt() {
		if (Core.netherStar)
			GameRegistry.addRecipe(new ItemStack(Items.nether_star, 1),
					new Object[] { "xxx", "ccc", "vcv", 'x',
							new ItemStack(Items.skull, 1, 1), 'c',
							Blocks.soul_sand, 'v', Items.emerald });
		if (Core.lead)
			GameRegistry.addRecipe(new ItemStack(Items.lead, 1), new Object[] {
					"  x", " x ", "x  ", 'x', Items.string });
		if (Core.string)
			GameRegistry.addShapelessRecipe(new ItemStack(Items.string, 4),
					new Object[] { Blocks.wool });
		if (Core.smeltFlesh)
			GameRegistry.addSmelting(Items.rotten_flesh, new ItemStack(
					Items.leather, 1), 50.0F);
		if (Core.blazeRod) {
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1),
					new Object[] { "x ", "x ", 'x', Items.blaze_powder });
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1),
					new Object[] { " x", " x", 'x', Items.blaze_powder });
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1),
					new Object[] { " x", "x ", 'x', Items.blaze_powder });
			GameRegistry.addRecipe(new ItemStack(Items.blaze_rod, 1),
					new Object[] { "x ", " x", 'x', Items.blaze_powder });
		}
		if (Core.cobbleGravel)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.gravel, 2),
					new Object[] { Blocks.cobblestone });
		if (Core.gravelSand)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sand, 2),
					new Object[] { Blocks.gravel });
		if (Core.gsDirt)
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dirt, 2),
					new Object[] { Blocks.gravel, Blocks.sand });
		if (Core.ghastTear)
			GameRegistry.addRecipe(new ItemStack(Items.ghast_tear, 1),
					new Object[] { " c ", "cxc", " c ", 'c',
							Items.blaze_powder, 'x', Items.water_bucket });
		if (Core.netherrack)
			GameRegistry.addShapelessRecipe(
					new ItemStack(Blocks.netherrack, 1), new Object[] {
							Items.blaze_powder, Blocks.stone });
		if (Core.soulSand) {
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.soul_sand, 1),
					new Object[] { Items.blaze_powder, Blocks.sand });
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.soul_sand, 1),
					new Object[] { Items.blaze_powder, Blocks.gravel });
		}
		if (Core.netherBrick) {
			GameRegistry.addRecipe(new ItemStack(Blocks.nether_brick, 1),
					new Object[] { " c ", "cxc", " c ", 'c',
							Items.blaze_powder, 'x', Blocks.stonebrick });
			GameRegistry.addShapelessRecipe(
					new ItemStack(Items.netherbrick, 1), new Object[] {
							Items.blaze_powder, Items.brick });
		}
		if (Core.emerald)
			GameRegistry.addRecipe(new ItemStack(Items.emerald, 1),
					new Object[] { "cvc", "vbv", "cvc", 'c',
							(new ItemStack(Items.dye, 1, 10)), 'v',
							Items.diamond, 'b', Items.gold_ingot });
		if (Core.spiderEye)
			GameRegistry.addRecipe(new ItemStack(Items.spider_eye, 1),
					new Object[] { " v ", "cbc", " z ", 'c',
							(new ItemStack(Items.dye, 1, 1)), 'v',
							Items.rotten_flesh, 'b', Items.string, 'z',
							Items.blaze_powder });
		if (Core.rottenFlesh)
			GameRegistry.addRecipe(new ItemStack(Items.rotten_flesh, 1),
					new Object[] { "x", "v", 'x', Items.sugar, 'v',
							Items.blaze_powder });
		if (Core.sapling)
			GameRegistry.addRecipe(new ItemStack(Blocks.sapling, 1, 0),
					new Object[] { "xxx", " c ", "vvv", 'x', Items.stick, 'v',
							Blocks.dirt, 'c', Blocks.log });
		if (Core.carrot)
			GameRegistry.addShapelessRecipe(new ItemStack(Items.carrot),
					new Object[] { Items.wheat_seeds, Blocks.leaves,
							new ItemStack(Items.dye, 1, 14) });
		if (Core.potato)
			GameRegistry.addShapelessRecipe(new ItemStack(Items.potato),
					new Object[] { Items.wheat_seeds, Blocks.log });
		if (Core.fermSpiderEye)
			GameRegistry.addShapelessRecipe(new ItemStack(
					Items.fermented_spider_eye), new Object[] {
					Items.spider_eye, Items.sugar, Items.redstone });
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		this.packetPipelineTeleport.initalise("CountryGamerCore");
		this.packetPipelineTeleport.registerPacket(PacketTeleport.class);

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.packetPipelineTeleport.postInitialise();

		boolean misc = CoreUtil.isModLoaded("misc");
		boolean pepc = CoreUtil.isModLoaded("CountryGamer_PEforPC");
		boolean pvz = CoreUtil.isModLoaded("pvz");
		boolean wam = CoreUtil.isModLoaded("weepingangels");
		if (!(misc || pepc || pvz || wam)) {
			Core.log.info("Why do you have me installed? I dont do anything!");
		} else {
			Core.log.info("Hey look! I have friends! Who's all here?");
			// TODO
			if (misc) {
				// CountryGamer_Misc.CG_Misc.log
				// .info("Im here with random things!");
			}
			if (pepc) {
				// CountryGamer_PEforPC.PEforPC.log
				// .info("I have all things pockity");
			}
			if (wam) {
				com.countrygamer.weepingangels.WeepingAngelsMod.log
						.info("ROAR!");
			}
			if (pvz) {
				// CountryGamer_PlantsVsZombies.PvZ_Main.log
				// .info("NO! Bad angel!");
			}
		}

		this.modCompatibility();

		if ((!tabItems.isEmpty() || !tabBlocks.isEmpty()) && true) {
			CreativeTabs coreTab = new CreativeTabs("CountryGamer") {
				@Override
				@SideOnly(Side.CLIENT)
				public Item getTabIconItem() {
					return Items.iron_pickaxe;
				}
			};
			for (Item item : tabItems) {
				item.setCreativeTab(CreativeTabs.tabAllSearch);
				item.setCreativeTab(coreTab);
			}
			for (Block item : tabBlocks) {
				item.setCreativeTab(CreativeTabs.tabAllSearch);
				item.setCreativeTab(coreTab);
			}
		}

	}

	public void dimLoad() {
		WorldServer[] allWS = DimensionManager.getWorlds();
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for (int i = 0; i < allWS.length; i++) {
			temp.put(allWS[i].provider.getDimensionName(),
					allWS[i].provider.dimensionId);
		}
		this.dimensions.clear();
		this.dimensions1.clear();
		SortedSet<String> keys = new TreeSet<String>(temp.keySet());
		for (String key : keys) {
			int id = temp.get(key);
			this.dimensions.put(key, id);
			this.dimensions1.put(id, key);
		}
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		// event.registerServerCommand(new TeleportCommand());
		this.dimLoad();
	}

	private void modCompatibility() {
		if (CoreUtil.isModLoaded("NotEnoughItems"))
			this.neiLoaded = true;
		if (CoreUtil.isModLoaded("Morph"))
			this.morphLoaded = true;
	}

	/**
	 * Must be called in post init
	 * 
	 * @return
	 */
	public static boolean isNEILoaded() {
		return neiLoaded;
	}

	/**
	 * Must be called in post init
	 * 
	 * @return
	 */
	public static boolean isMorphLoaded() {
		return morphLoaded;
	}

	// Rain; Minecraft.getMinecraft().theWorld.setRainStrength(0.0F);

	public static void addItemToTab(Item item) {
		tabItems.add(item);
	}

	public static void addBlockToTab(Block block) {
		tabBlocks.add(block);
	}

}
