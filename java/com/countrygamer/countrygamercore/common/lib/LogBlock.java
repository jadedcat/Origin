package com.countrygamer.countrygamercore.common.lib;

import java.util.logging.Logger;

import com.countrygamer.countrygamercore.common.Core;

/**
 * Created by Country_Gamer on 3/21/14.
 */
public class LogBlock {
	
	public static void print(Logger logger, String message) {
		LogBlock.print(logger, message, true);
	}
	
	public static void print(Logger logger, String message, boolean debugSensitive) {
		LogBlock.print(logger, new String[] {
			message
		}, debugSensitive);
	}
	
	public static void print(Logger logger, String[] message) {
		LogBlock.print(logger, message, true);
	}
	
	public static void print(Logger logger, String[] message, boolean debugSensitive) {
		LogBlock log = new LogBlock(logger);
		for (int i = 0; i < message.length; i++) {
			log.addWithLine(message[i]);
		}
		if (!debugSensitive)
			log.log();
		else if (debugSensitive && Core.DEBUG) log.log();
	}
	
	private final Logger	logger;
	private String			textToLog	= "";
	
	public LogBlock(Logger log) {
		this.logger = log;
	}
	
	public LogBlock(Logger log, String text) {
		this(log);
		this.textToLog += "\n";
	}
	
	public LogBlock add(String text) {
		this.textToLog += text;
		return this;
	}
	
	public LogBlock addWithLine(String text) {
		return this.add(text + "\n");
	}
	
	public void clear() {
		this.textToLog = "";
	}
	
	public void log() {
		this.logger.info(this.textToLog);
	}
	
}
