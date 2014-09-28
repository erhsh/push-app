package com.blackcrystalinfo.push.parser;

import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;

public class AlarmMsgParser implements IMsgParser {

	@Override
	public SendMessage parse(MessageBean rawMsg) {
		SendMessage result = new SendMessage();

		result.setTarget("*");
		result.setTitle("alarm title");
		result.setContent(rawMsg.getMsgData().toString());

		return result;
	}

}
