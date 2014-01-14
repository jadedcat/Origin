package CountryGamer_Core;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import CountryGamer_Core.Handler.CorePacketHandler;
import CountryGamer_Core.Handler.Command.TeleportCommand;
import CountryGamer_Core.Proxy.ServerProxy;
import CountryGamer_Core.lib.CoreReference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = CoreReference.MOD_ID, name = CoreReference.MOD_NAME, version = CoreReference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { "CGC_Teleport" }, packetHandler = CorePacketHandler.class)
public class CG_Core {

	public static final Logger log = Logger.getLogger(CoreReference.MOD_ID);
	@Instance(CoreReference.MOD_ID)
	public static CG_Core instance;
	@SidedProxy(clientSide = CoreReference.CLIENT_PROXY_CLASS, serverSide = CoreReference.SERVER_PROXY_CLASS)
	public static ServerProxy proxy;

	public static boolean DEBUG = true;

	public static HashMap<String, Integer> dimensions = new HashMap<String, Integer>();
	public static HashMap<Integer, String> dimensions1 = new HashMap<Integer, String>();

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

}
