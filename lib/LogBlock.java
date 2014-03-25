package com.countrygamer.core.lib;

import java.util.logging.Logger;

/**
 * Created by Country_Gamer on 3/21/14.
 */
public class LogBlock {

	private final Logger logger;
	private String textToLog = "";

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
