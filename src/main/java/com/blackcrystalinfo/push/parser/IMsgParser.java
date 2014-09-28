package com.blackcrystalinfo.push.parser;

import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;

public interface IMsgParser {
	SendMessage parse(MessageBean rawMsg);
}
