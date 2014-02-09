package CountryGamer_Core;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import CountryGamer_Core.Handler.CorePacketHandler;
import CountryGamer_Core.Handler.Command.TeleportCommand;
import CountryGamer_Core.Proxy.ServerProxy;
import CountryGamer_Core.lib.CoreReference;
import CountryGamer_Core.lib.CoreUtil;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Mod class for the basic mod CountryGamer_Core
 * 
 * @author Country Gamer
 * 
 */
@Mod(modid = CoreReference.MOD_ID, name = CoreReference.MOD_NAME, version = CoreReference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { "CGC_Teleport" }, packetHandler = CorePacketHandler.class)
public class CG_Core {
	
	public static final Logger				log			= Logger.getLogger(CoreReference.MOD_ID);
	@Instance(CoreReference.MOD_ID)
	public static CG_Core					instance;
	@SidedProxy(clientSide = CoreReference.CLIENT_PROXY_CLASS, serverSide = CoreReference.SERVER_PROXY_CLASS)
	public static ServerProxy				proxy;
	
	public static boolean					DEBUG		= true;
	
	public static HashMap<String, Integer>	dimensions	= new HashMap<String, Integer>();
	public static HashMap<Integer, String>	dimensions1	= new HashMap<Integer, String>();
	
	// Mods Loaded
	private static boolean					neiLoaded	= false;
	private static boolean					morphLoaded	= false;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load(); // load configs from its file
		
		config.save();
		
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRender();
		proxy.registerHandler();
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		boolean misc = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_Misc");
		boolean pepc = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_PEforPC");
		boolean pvz = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"CountryGamer_PlantsVsZombies");
		boolean wam = CoreUtil.isModLoaded(CoreReference.MOD_ID,
				"WeepingAngels");
		if (!(misc || pepc || pvz || wam)) {
			CG_Core.log
					.info("Why do you have me installed? I dont do anything!");
		} else {
			CG_Core.log.info("Hey look! I have friends! Who's all here?");
			if (misc)
				CountryGamer_Misc.CG_Misc.log
						.info("Im here with random things!");
			if (pepc)
				CountryGamer_PEforPC.PEforPC.log
						.info("I have all things pockity");
			if (wam)
				WeepingAngels.WeepingAngelsMod.log.info("ROAR!");
			if (pvz)
				CountryGamer_PlantsVsZombies.PvZ_Main.log
						.info("NO! Bad angel!");
			
		}
		
		this.modCompatibility();
		
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
		event.registerServerCommand(new TeleportCommand());
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
	
}
