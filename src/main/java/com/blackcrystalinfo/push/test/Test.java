package com.blackcrystalinfo.push.test;

import java.util.Locale;
import java.util.ResourceBundle;

public class Test {

	public static void main(String[] args) {
		Locale locale = Locale.getDefault();

		ResourceBundle bundle = ResourceBundle.getBundle("message", locale);

		String msg = bundle.getString("alarm.7.infrared");

		System.out.println(msg);
		
		System.out.println("over");
	}
}
