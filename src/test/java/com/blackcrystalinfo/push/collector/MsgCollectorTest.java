package com.blackcrystalinfo.push.collector;

import org.junit.Before;
import org.junit.Test;

import com.blackcrystalinfo.push.message.MessageBean;

public class MsgCollectorTest {

	private MsgCollector collector;

	@Before
	public void setUp() throws Exception {

		collector = new MsgCollector();

		collector.connect();
	}

	@Test
	public void testReceive() {

		MessageBean messageBean = collector.receive();

		System.out.println(messageBean);
	}

}
