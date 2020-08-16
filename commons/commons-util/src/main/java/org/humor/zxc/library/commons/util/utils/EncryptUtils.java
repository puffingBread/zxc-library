package org.humor.zxc.library.commons.util.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class EncryptUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);

	public static String getMD5(String str) {
		return encode(str, "MD5");
	}

	public static String getDES(String str) {
		return encode(str, "DES");
	}

	public static String getSHA1(String str) {
		return encode(str, "SHA-1");
	}

	private static String encode(String str, String type) {
		try {
			MessageDigest alga = MessageDigest.getInstance(type);
			alga.update(str.getBytes());
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		}
		catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return "";
	}

	public static String uuid(String[] strs) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			for (String str : strs) {
				if (str != null) {
					md.update(str.getBytes());
				}
			}

			byte[] bs = md.digest();
			return byte2hex(bs);
		}
		catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return null;
	}

	private static String byte2hex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (byte value : b) {
			String stmp = Integer.toHexString(value & 0xff);
			if (stmp.length() == 1) {
				sb.append("0");
			}
			sb.append(stmp);
		}

		return sb.toString().toUpperCase();
	}

}
