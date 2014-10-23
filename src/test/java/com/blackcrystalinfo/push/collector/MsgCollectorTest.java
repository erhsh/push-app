//package com.blackcrystalinfo.push.collector;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.blackcrystalinfo.push.message.MessageBean;
//import com.blackcrystalinfo.push.receiver.IReceiver;
//import com.blackcrystalinfo.push.receiver.impl.RmqReceiver;
//
//public class MsgCollectorTest {
//
//	private IReceiver receiver;
//
//	@Before
//	public void setUp() throws Exception {
//
//		receiver = new RmqReceiver();
//
//		receiver.connect();
//	}
//
//	@Test
//	public void testReceive() {
//
//		MessageBean messageBean = receiver.receive();
//
//		System.out.println(messageBean);
//	}
//
//}
