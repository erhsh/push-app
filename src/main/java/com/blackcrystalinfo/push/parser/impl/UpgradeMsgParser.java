package com.blackcrystalinfo.push.parser.impl;

import java.util.List;

import com.blackcrystalinfo.push.exception.PushParserException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.IMsgParser;

/**
 * 升级消息解析器
 * 
 * @author j
 * 
 */
public class UpgradeMsgParser implements IMsgParser {

	@Override
	public List<SendMessage> parse(MessageBean rawMsg)
			throws PushParserException {
		return null;
	}

}
