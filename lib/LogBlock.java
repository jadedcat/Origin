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

	public void add(String text) {
		this.textToLog += text;
	}

	public void clear() {
		this.textToLog = "";
	}

	public void log() {
		this.logger.info(this.textToLog);
	}

}
