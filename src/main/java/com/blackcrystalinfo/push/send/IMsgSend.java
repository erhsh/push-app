package com.blackcrystalinfo.push.send;

import com.blackcrystalinfo.push.msg.IMessage;


/**
 * 发送消息给用户
 * @author j
 *
 */
public interface IMsgSend {
	void send(IMessage msg);
}
