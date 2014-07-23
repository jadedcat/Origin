package com.countrygamer.cgo.common.lib.util;

import com.countrygamer.cgo.common.Origin;
import com.countrygamer.cgo.common.lib.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilCrash {
	
	public static CrashReport makeCrashReport(String message, String throwableError) {
		return new CrashReport(message, new Throwable(throwableError));
	}
	
	public static void throwCrashReport(String message, String throwableError) {
		UtilCrash.throwCrashReport(UtilCrash.makeCrashReport(message, throwableError));
	}
	
	public static void throwCrashReport(CrashReport crashreport) {
		File file1 = new File(new File(new File("."), "crash-reports"), "crash-"
				+ (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
		
		if (crashreport.saveToFile(file1)) {
			LogHelper.info(Origin.pluginName(), "This crash report has been saved to: " + file1.getAbsolutePath());
		}
		else {
			LogHelper.error(Origin.pluginName(), "We were unable to save this crash report to disk.");
		}
		
		FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick
		// to avoid race conditions
		Minecraft.getMinecraft().crashed(crashreport);
	}
	
}
