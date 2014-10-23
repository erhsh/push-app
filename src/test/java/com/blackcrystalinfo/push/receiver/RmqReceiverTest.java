package com.blackcrystalinfo.push.receiver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.receiver.impl.RmqReceiver;

public class RmqReceiverTest {

	private IReceiver receiver;

	@Before
	public void setUp() throws Exception {
		receiver = new RmqReceiver();
		receiver.connect();
	}

	@Test
	public void testReceive() {
		MessageBean messageBean = receiver.receive();
		System.out.println(messageBean);
	}

	@After
	public void tearDown() throws Exception {
		receiver.close();
	}

}
