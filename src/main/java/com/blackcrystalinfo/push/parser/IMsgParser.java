package com.blackcrystalinfo.push.parser;

import java.util.List;

import com.blackcrystalinfo.push.exception.PushParserException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;

public interface IMsgParser {
	List<SendMessage> parse(MessageBean rawMsg) throws PushParserException;
}
