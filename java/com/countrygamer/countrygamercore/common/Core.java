package com.countrygamer.countrygamercore.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.countrygamer.core.Base.Plugin.PluginBase;
import com.countrygamer.core.Base.common.network.MessageSyncExtendedProperties;
import com.countrygamer.countrygamercore.common.network.MessageTeleport;
import com.countrygamer.countrygamercore.common.network.MessageUpdateRedstoneState;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Core.pluginID, name = Core.pluginName, version = "@PLUGIN_VERSION@")
public class Core extends PluginBase {
	
	public static final String pluginID = "countrygamercore";
	public static final String pluginName = "CountryGamer Core";
	
	public static final Logger logger = Logger.getLogger(Core.pluginName);
	
	@SidedProxy(clientSide = "com.countrygamer.countrygamercore.client.ClientProxy",
			serverSide = "com.countrygamer.countrygamercore.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(Core.pluginID)
	public static Core instance;
	
	public static boolean DEBUG = false;
	
	//
	public static HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
	public static HashMap<Integer, String> dimensions1 = new HashMap<Integer, String>();
	
	private static ArrayList<Item> tabItems = new ArrayList<Item>();
	private static ArrayList<Block> tabBlocks = new ArrayList<Block>();
	
	//
	
	@SuppressWarnings("unchecked")
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		super.preInitialize(Core.pluginID, Core.pluginName, event, Core.proxy, new CoreOptions(),
				null, null, null, null);
		this.registerHandlers(this, null);
		
		((CoreOptions) this.options).vanillaCraftSmelt();
		
		this.regsiterPacketHandler(Core.pluginID, MessageSyncExtendedProperties.class,
				MessageTeleport.class, MessageUpdateRedstoneState.class);
		
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		super.initialize(event);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		super.postInitialize(event);
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
	
	// Events
	public void dimLoad() {
		WorldServer[] allWS = DimensionManager.getWorlds();
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for (int i = 0; i < allWS.length; i++) {
			temp.put(allWS[i].provider.getDimensionName(), allWS[i].provider.dimensionId);
		}
		Core.dimensions.clear();
		Core.dimensions1.clear();
		SortedSet<String> keys = new TreeSet<String>(temp.keySet());
		for (String key : keys) {
			int id = temp.get(key);
			Core.dimensions.put(key, id);
			Core.dimensions1.put(id, key);
		}
	}
	
	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		this.dimLoad();
	}
	
	public static void addItemToTab(Item item) {
		tabItems.add(item);
	}
	
	public static void addBlockToTab(Block block) {
		tabBlocks.add(block);
	}
	
	@Mod.EventHandler
	public void imcReviever(FMLInterModComms.IMCEvent event) {
		// Core.logger.info("\n\nIMC Register\n\n");
		for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
			if (imcMessage.key.equalsIgnoreCase("register-plugin")) {
				if (imcMessage.isStringMessage()) {
					Core.logger.info("Well hello there " + imcMessage.getSender()
							+ "! Welcome to the party. Thank you for the message: \""
							+ imcMessage.getStringValue() + "\".");
				}
			}
		}
	}
	
	/**
	 * Potion IDs for crafting
	 * 
	 * @version Minecraft 1.6.4
	 * @author coolAlias
	 * @see 'http://www.minecraftforum.net/topic/1891579-using-potions-in-crafting-recipes/'
	 * 
	 */
	public enum EnumPotionID {
		/* POTION DATA VALUES from http://www.minecraftwiki.net/wiki/Potions
		 * EXT means "Extended" version of potion
		 * REV means "Reverted" version of potion
		 */
		POTION_AWKWARD(16), POTION_THICK(32), POTION_MUNDANE(128), POTION_MUNDANE_EXT(64),
		
		/*
		 * HELPFUL POTIONS
		 */
		POTION_REGEN(8193), POTION_REGEN_II(8225), POTION_REGEN_EXT(8257), POTION_REGEN_II_EXT(8289), POTION_REGEN_SPLASH(
				16385), POTION_REGEN_SPLASH_II(16417), POTION_REGEN_SPLASH_EXT(16449),
		
		POTION_SWIFTNESS(8194), POTION_SWIFTNESS_II(8226), POTION_SWIFTNESS_EXT(8258), POTION_SWIFTNESS_II_EXT(
				8290), POTION_SWIFTNESS_SPLASH(16386), POTION_SWIFTNESS_SPLASH_II(16418), POTION_SWIFTNESS_SPLASH_EXT(
				16450),
		
		POTION_FIRERESIST(8195), POTION_FIRERESIST_REV(8227), POTION_FIRERESIST_EXT(8259), POTION_FIRERESIST_SPLASH(
				16387), POTION_FIRERESIST_SPLASH_REV(16419), POTION_FIRERESIST_SPLASH_EXT(16451),
		
		POTION_HEALING(8197), POTION_HEALING_II(8229), POTION_HEALING_REV(8261), POTION_HEALING_SPLASH(
				16389), POTION_HEALING_SPLASH_II(16421), POTION_HEALING_SPLASH_REV(16453),
		
		POTION_NIGHTVISION(8198), POTION_NIGHTVISION_REV(8230), POTION_NIGHTVISION_EXT(8262), POTION_NIGHTVISION_SPLASH(
				16390), POTION_NIGHTVISION_SPLASH_REV(16422), POTION_NIGHTVISION_SPLASH_EXT(16454),
		
		POTION_STRENGTH(8201), POTION_STRENGTH_II(8233), POTION_STRENGTH_EXT(8265), POTION_STRENGTH_II_EXT(
				8292), POTION_STRENGTH_SPLASH(16393), POTION_STRENGTH_SPLASH_II(16425), POTION_STRENGTH_SPLASH_EXT(
				16457),
		
		POTION_INVISIBILITY(8206), POTION_INVISIBILITY_REV(8238), POTION_INVISIBILITY_EXT(8270), POTION_INVISIBILITY_SPLASH(
				16398), POTION_INVISIBILITY_SPLASH_REV(16430), POTION_INVISIBILITY_SPLASH_EXT(16462),
		
		/*
		 * HARMFUL POTIONS
		 */
		POTION_POISON(8196), POTION_POISON_II(8228), POTION_POISON_EXT(8260), POTION_POISON_SPLASH(
				16388), POTION_POISON_SPLASH_II(16420), POTION_POISON_SPLASH_EXT(16452),
		
		POTION_WEAKNESS(8200), POTION_WEAKNESS_REV(8232), POTION_WEAKNESS_EXT(8264), POTION_WEAKNESS_SPLASH(
				16392), POTION_WEAKNESS_SPLASH_REV(16424), POTION_WEAKNESS_SPLASH_EXT(16456),
		
		POTION_SLOWNESS(8202), POTION_SLOWNESS_REV(8234), POTION_SLOWNESS_EXT(8266), POTION_SLOWNESS_SPLASH(
				16394), POTION_SLOWNESS_SPLASH_REV(16426), POTION_SLOWNESS_SPLASH_EXT(16458),
		
		POTION_HARM(8204), POTION_HARM_II(8236), POTION_HARM_REV(8268), POTION_HARM_SPLASH(16396), POTION_HARM_SPLASH_II(
				16428), POTION_HARM_SPLASH_REV(16460);
		
		private final int potionID;
		
		private EnumPotionID(int par1) {
			this.potionID = par1;
		}
		
		public int getPotionID() {
			return this.potionID;
		}
	}
	
}
