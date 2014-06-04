package com.countrygamer.countrygamercore.lib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import com.countrygamer.countrygamercore.common.Core;

import cpw.mods.fml.common.FMLCommonHandler;

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
			Core.logger.info("This crash report has been saved to: " + file1.getAbsolutePath());
		}
		else {
			Core.logger.info("We were unable to save this crash report to disk.");
		}
		
		FMLCommonHandler.instance().expectServerStopped(); // has to come before finalTick
		// to avoid race conditions
		Minecraft.getMinecraft().crashed(crashreport);
	}
	
}
