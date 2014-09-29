package com.blackcrystalinfo.push.service;

import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.msg.impl.BaiduMsgPusher;
import com.blackcrystalinfo.push.pusher.msg.impl.GetuiMsgPusher;
import com.blackcrystalinfo.push.pusher.msg.impl.JiguangMsgPusher;

public class MsgPushService extends APushService {

	public MsgPushService(String providerName) {
		super(providerName);
	}

	public void initProvider() {
		if ("Baidu".equalsIgnoreCase(providerName)) {
			pusher = new BaiduMsgPusher();
		} else if ("Getui".equalsIgnoreCase(providerName)) {
			pusher = new GetuiMsgPusher();
		} else if ("Jiguang".equalsIgnoreCase(providerName)) {
			pusher = new JiguangMsgPusher();
		}
	}

	@Override
	protected void initData(SendMessage msg) {

		if ("Baidu".equalsIgnoreCase(providerName)) {
			BaiduMsgData baiduMsgData = new BaiduMsgData();
			baiduMsgData.setDeviceType(3);
			baiduMsgData.setChannelId(4334295471091639679L);
			baiduMsgData.setUserId("763093826355642008");

			baiduMsgData.setMessageType(1);
			baiduMsgData.setMessage("{\"title\":\"" + msg.getTitle()
					+ "\",\"description\":\"" + msg.getContent() + "\"}");

			data = baiduMsgData;

		} else if ("Getui".equalsIgnoreCase(providerName)) {

		} else if ("Jiguang".equalsIgnoreCase(providerName)) {

		}

	}

}
