package com.blackcrystalinfo.push.utils;

public class Constants extends ConfigurableConstants {

	static {
		init("sys.properties");
	}

	public static String API_KEY = getProperty("apiKey", "");

	public static String SECRET_KEY = getProperty("secretKey", "");

	public static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	public static void main(String[] args) {
		System.out.println(Constants.API_KEY);
		System.out.println(Constants.SECRET_KEY);
		
		System.out.println(System.getProperty("user.dir"));
	}
}
