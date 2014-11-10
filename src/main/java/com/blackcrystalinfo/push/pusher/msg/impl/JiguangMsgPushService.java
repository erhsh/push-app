package com.blackcrystalinfo.push.pusher.msg.impl;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.impl.JiguangMsgData;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.msg.AMsgPushService;

public class JiguangMsgPushService extends AMsgPushService {

	@Override
	protected void initPusher() {
		this.pusher = new JiguangMsgPusher();
	}

	@Override
	protected IData buildData(SendMessage msg) {
		JiguangMsgData data = new JiguangMsgData();

		return data;
	}

}
