package com.blackcrystalinfo.push.utils.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(ReflectionUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T newInst(String clazzName) {
		T t = null;

		try {
			Class<?> clazz = Class.forName(clazzName);

			t = (T) clazz.newInstance();

		} catch (Exception e) {
			logger.error("New instance error, clazzName={}", clazzName);
			throw new RuntimeException("New instance by className error", e);
		}

		return t;
	}

	public static void main(String[] args) {

		Object s = ReflectionUtils
				.newInst("com.blackcrystalinfo.push.utils.reflect.ReflectionUtils");
		System.out.println(s);

	}
}
