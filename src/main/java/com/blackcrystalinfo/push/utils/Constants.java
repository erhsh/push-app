package com.blackcrystalinfo.push.utils;

public class Constants extends ConfigurableConstants {

	static {
		init("sys.properties");
	}

	public static String RMQ_HOST = getProperty("rmq.host", "");
	public static String RMQ_PORT = getProperty("rmq.port", "");
	public static String RMQ_USER = getProperty("rmq.user", "");
	public static String RMQ_PASS = getProperty("rmq.pass", "");

	public static String RMQ_EXCHANGE_NAME = getProperty("rmq.exchange.name",
			"");
	public static String RMQ_ROUTING_KEY = getProperty("rmq.routing.key", "");

	public static String API_KEY = getProperty("apiKey", "");
	public static String SECRET_KEY = getProperty("secretKey", "");
	public static String DEPLOY_STATUS = getProperty("deployStatus", "1");

	public static String REDIS_HOST = getProperty("redis.host", "");
	public static String REDIS_PORT = getProperty("redis.port", "");
	public static String REDIS_PASS = getProperty("redis.pass", "");

	public static String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	public static void main(String[] args) {
		System.out.println(Constants.RMQ_HOST);
		System.out.println(Constants.RMQ_PORT);
		System.out.println(Constants.RMQ_USER);
		System.out.println(Constants.RMQ_PASS);

		System.out.println(Constants.API_KEY);
		System.out.println(Constants.SECRET_KEY);
		System.out.println(Constants.DEPLOY_STATUS);

		System.out.println(Constants.REDIS_HOST);
		System.out.println(Constants.REDIS_PORT);
		System.out.println(Constants.REDIS_PASS);

		System.out.println(System.getProperty("user.dir"));
	}
}
