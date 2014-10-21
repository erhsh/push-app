package com.blackcrystalinfo.push.utils;

public final class StringUtils {
	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase()).append(" ");
		}

		return sb.toString().trim();
	}
	
	public static void main(String[] args) {
		System.out.println(toHexString("123".getBytes()));
	}
}
