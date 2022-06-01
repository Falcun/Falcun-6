package net.mattbenson.network.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class StringUtils {
	public StringUtils() {}
	
	public static boolean isInteger(String content) {
		try {
			Integer.valueOf(content);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isDouble(String content) {
		try {
			Double.valueOf(content);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isLong(String content) {
		try {
			Long.valueOf(content);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static String sanitize(String content) {
		return content.replace("\n", "\\n").replace("\r", "\\r");
	}
	
	public static UUID getUUID(String string) {
		try {
			return UUID.fromString(string);
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	public static String ii() throws NoSuchAlgorithmException, IOException {

		String s = "";
		final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME")
		+ System.getProperty("user.name").trim();
		final byte[] bytes = main.getBytes("UTF-8");
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		final byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		for (final byte b : md5) {
			s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
			if (i != md5.length - 1) {
				s += "-";
			}
			i++;
		}
		return s;
	}

}
