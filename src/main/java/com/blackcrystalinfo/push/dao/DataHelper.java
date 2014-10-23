package com.blackcrystalinfo.push.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import com.blackcrystalinfo.push.utils.Constants;

public class DataHelper {
	private static final JedisPool pool;
	private static final int ONE_SECOND = 1000;

	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(1000);
		poolConfig.setMaxWaitMillis(ONE_SECOND);

		String host = Constants.REDIS_HOST;
		int port = Integer.parseInt(Constants.REDIS_PORT);
		String password = null;

		pool = new JedisPool(poolConfig, host, port, ONE_SECOND, password);
	}

	public static Jedis getJedis() {
		return pool.getResource();
	}

	public static void returnJedis(Jedis res) {
		if (res != null) {
			pool.returnResource(res);
		}
	}

	public static void returnBrokenJedis(Jedis res) {
		if (res != null) {
			pool.returnBrokenResource(res);
		}
	}

	public static void main(String[] args) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = DataHelper.getJedis();

			jedis.set("hello", "world");

			System.out.println(jedis);
		} catch (JedisException e) {
			success = false;
			if (null != jedis) {
				DataHelper.returnBrokenJedis(jedis);
			}

		} finally {
			if (success && null != jedis) {
				DataHelper.returnJedis(jedis);
				System.out.println("return ok");
			}
		}
	}
}
