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
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

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
	@SidedProxy(clientSide = "com.countrygamer."
			+ CoreReference.CLIENT_PROXY_CLASS, serverSide = "com.countrygamer."
			+ CoreReference.SERVER_PROXY_CLASS)
	public static ServerProxy proxy;

	public static boolean DEBUG = true;

	public static HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
	public static HashMap<Integer, String> dimensions1 = new HashMap<Integer, String>();

	private static ArrayList<Item> tabItems = new ArrayList<Item>();
	private static ArrayList<Block> tabBlocks = new ArrayList<Block>();

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
		proxy.registerRender();
		proxy.registerHandler();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		this.packetPipelineTeleport.initalise("CountryGamerCore");
		this.packetPipelineTeleport.registerPacket(PacketTeleport.class);

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.packetPipelineTeleport.postInitialise();

		boolean misc = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_Misc");
		boolean pepc = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_PEforPC");
		boolean pvz = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_PlantsVsZombies");
		boolean wam = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"weepingangels");
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
		if (CoreUtil.isModLoaded(CoreReference.MOD_ID, "NotEnoughItems"))
			this.neiLoaded = true;
		if (CoreUtil.isModLoaded(CoreReference.MOD_ID, "Morph"))
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
