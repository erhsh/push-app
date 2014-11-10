package com.blackcrystalinfo.push.parser;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.impl.AlarmMsgParser;

public class AlarmMsgParserTest {

	private AlarmMsgParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new AlarmMsgParser();
	}

	@Test
	public void testParse() {
		MessageBean rawMsg = new MessageBean();

		List<SendMessage> sendMsgs = null;

		try {
			sendMsgs = parser.parse(rawMsg);

			for (SendMessage sendMsg : sendMsgs) {
				System.out.println(sendMsg);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
