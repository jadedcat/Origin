package CountryGamer_Core.Proxy;

import net.minecraftforge.common.MinecraftForge;
import CountryGamer_Core.Handler.CoreEventHandler;

public class ServerProxy {
	
	public void preInit() {
		
	}
	public void registerRender() {
		
	}
	public void registerHandler() {
		MinecraftForge.EVENT_BUS.register(new CoreEventHandler());
	}
	
}
