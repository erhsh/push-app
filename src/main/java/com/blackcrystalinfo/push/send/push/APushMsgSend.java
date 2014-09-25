package com.blackcrystalinfo.push.send.push;

import com.blackcrystalinfo.push.msg.IMessage;
import com.blackcrystalinfo.push.msg.push.APushMessage;
import com.blackcrystalinfo.push.send.IMsgSend;

/**
 * 发送推送消息给用户
 * 
 * @author j
 * 
 */
public abstract class APushMsgSend implements IMsgSend {

	@Override
	public void send(IMessage msg) {
		if (msg instanceof APushMessage) {
			APushMessage pushMsg = (APushMessage) msg;
			push(pushMsg);
		}
	}

	protected abstract void push(APushMessage pushMsg);
}
