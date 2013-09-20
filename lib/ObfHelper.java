package CountryGamer_Core.lib;

import cpw.mods.fml.common.FMLLog;

public class ObfHelper {

	public static boolean obfuscation;

	public static final String[] mainModel = new String[] { "i", "field_77045_g", "mainModel" }; //RendererLivingEntity

	public static void obfWarning()
	{
		FMLLog.info("Forgot to update obfuscation!", true);
	}
	
	  public static void detectObfuscation(){
	        try{
	            Class.forName("net.minecraft.world.World");
	            obfuscation = false;
	        } catch (Exception e) {
	            obfuscation = true;
	        }

	    }

}
