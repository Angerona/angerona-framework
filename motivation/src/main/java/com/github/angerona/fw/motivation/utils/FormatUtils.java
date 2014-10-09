package com.github.angerona.fw.motivation.utils;

import java.text.DecimalFormat;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class FormatUtils {

	private static final DecimalFormat FORMAT = new DecimalFormat("<0.0##>;<-0.0##>");

	public static String format(double d) {
		return FORMAT.format(d);
	}

}
