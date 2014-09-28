package com.blackcrystalinfo.push;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.IPusher;
import com.blackcrystalinfo.push.pusher.msg.impl.BaiduMsgPusher;

public class MsgSendService {

	private IPusher sender;

	private boolean isBadu = true;

	public MsgSendService() {
		init();
	}

	private void init() {
		if (isBadu) {
			sender = new BaiduMsgPusher();
		}
	}

	public void send(SendMessage pushMsg) {

		IData msg = null;
		if (isBadu) {
			BaiduMsgData baiduPusMsg = new BaiduMsgData();
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
