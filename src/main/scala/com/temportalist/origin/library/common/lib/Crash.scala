package com.temportalist.origin.library.common.lib

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import com.temportalist.origin.library.common.Origin
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.client.Minecraft
import net.minecraft.crash.CrashReport

/**
 *
 *
 * @author CountryGamer
 */
class Crash(message: String, error: String) {

	Crash.throwCrashReport(new CrashReport(message, new Throwable(error)))

}

object Crash {

	def throwCrashReport(report: CrashReport): Unit = {
		val file1: File = new File(
			new File(
				new File("."), "crash-reports"
			),
			"crash-" +
					new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date) +
					"-server.txt"
		)

		if (report.saveToFile(file1)) {
			LogHelper.info(Origin.pluginName,
				"This crash report has been saved to: " + file1.getAbsolutePath)
		}
		else {
			LogHelper.error(Origin.pluginName, "We were unable to save this crash report to disk.")
		}

		FMLCommonHandler.instance.expectServerStopped
		Minecraft.getMinecraft.crashed(report)
	}

}
