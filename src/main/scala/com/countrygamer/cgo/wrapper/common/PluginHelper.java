package com.countrygamer.cgo.wrapper.common;

import com.countrygamer.cgo.wrapper.common.registries.Register;
import scala.collection.Seq;

import java.util.Arrays;

/**
 * @author CountryGamer
 */
public class PluginHelper {

	public static Seq<Register> seqFrom(Register... registers) {
		return scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(registers));
	}

}
