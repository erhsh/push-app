package com.blackcrystalinfo.push;

import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.msg.IMessage;
import com.blackcrystalinfo.push.msg.push.impl.BaiduPushMessage;
import com.blackcrystalinfo.push.send.IMsgSend;
import com.blackcrystalinfo.push.send.push.impl.BaiduPushMsgSend;

public class MsgSendService {

	private IMsgSend sender;

	private boolean isBadu = true;

	public MsgSendService() {
		init();
	}

	private void init() {
		if (isBadu) {
			sender = new BaiduPushMsgSend();
		}
	}

	public void send(SendMessage pushMsg) {

		IMessage msg = null;
		if (isBadu) {
			BaiduPushMessage baiduPusMsg = new BaiduPushMessage();
			baiduPusMsg.setDeviceType(3);
			baiduPusMsg.setChannelId(4334295471091639679L);
			baiduPusMsg.setUserId("763093826355642008");

			baiduPusMsg.setMessageType(1);
			baiduPusMsg.setMessage("{\"title\":\"" + pushMsg.getTitle()
					+ "\",\"description\":\"" + pushMsg.getTitle() + "\"}");

			msg = baiduPusMsg;
		}
		sender.send(msg);
	}
}
