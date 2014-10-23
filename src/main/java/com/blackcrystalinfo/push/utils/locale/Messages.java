package com.blackcrystalinfo.push.utils.locale;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages {

	private static final Logger logger = LoggerFactory
			.getLogger(Messages.class);

	private static ResourceBundle bundle = ResourceBundle.getBundle("message",
			Locale.getDefault());

	public static String locale(String key) {
		String result = "";

		try {
			result = bundle.getString(key);
		} catch (Exception e) {
			logger.error("missing key = {}", key);
			result = key;
		}

		return result;
	}

	public static void main(String[] args) {
		System.out.println(Messages.locale("hello1"));
	}
}
