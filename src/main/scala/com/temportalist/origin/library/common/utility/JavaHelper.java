package com.temportalist.origin.library.common.utility;

import com.temportalist.origin.library.common.register.Register;
import scala.collection.Seq;

import java.util.Arrays;

/**
 * @author TheTemportalist
 */
public class JavaHelper {

	public static Seq<Register> seqFrom(Register... registers) {
		return scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(registers));
	}

}
