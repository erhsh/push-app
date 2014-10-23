package com.blackcrystalinfo.push.dao;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class DataHelperTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetJedis() throws InterruptedException {

		Jedis jedis = null;
		try {
			jedis = DataHelper.getJedis();
			jedis.set("hello", "world");
			System.out.println("ok");
		} catch (JedisException e) {
			System.out.println(e);
		}

	}

	@Test
	public void testReturnJedis() {
		Jedis jedis = null;
		try {
			jedis = DataHelper.getJedis();
			jedis.set("hello", "world");
			System.out.println("ok");
			throw new JedisException("hello exception");
		} catch (JedisException e) {
			System.out.println(e);
		} finally {
			DataHelper.returnJedis(jedis);
		}
	}

	@Test
	public void testReturnBrokenJedis() {
		Jedis jedis = null;
		try {
			jedis = DataHelper.getJedis();
			Set<String> value = jedis.smembers("hello");
			System.out.println(value);
			throw new JedisException("hello exception");
		} catch (JedisException e) {
			e.printStackTrace();
			DataHelper.returnBrokenJedis(jedis);
		}
	}

	@Test
	public void test() {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = DataHelper.getJedis();
			jedis.set("hello", "world");
			System.out.println("ok");
			throw new JedisException("hello exception");
		} catch (JedisException e) {
			success = false;
			DataHelper.returnBrokenJedis(jedis);
		} finally {
			if (success) {
				DataHelper.returnJedis(jedis);
			}
		}
	}
}
