package com.blackcrystalinfo.push.utils.format;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化类
 * 
 * @author j
 * 
 */
public final class DateFormat {
	private static final String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";

	private SimpleDateFormat format;

	public DateFormat() {
		this(DEFAULT_PATTERN);
	}

	public DateFormat(String pattern) {

		if (pattern == null || pattern.equals("")) {
			throw new IllegalArgumentException(
					"parameter pattern can not be null or empty");
		}
		this.format = new SimpleDateFormat(DEFAULT_PATTERN);
	}

	public synchronized void setPattern(String pattern) {
		if (pattern == null || pattern.equals("")) {
			throw new IllegalArgumentException(
					"parameter pattern can not be null or empty");
		}
		this.format = new SimpleDateFormat(pattern);
	}

	public synchronized String format(Date date) {
		if (null == date) {
			return "";
		}

		return format.format(date);
	}

	public synchronized String format(Long date) {
		if (null == date) {
			return "";

		}

		return format.format(new Date(date));
	}

}
