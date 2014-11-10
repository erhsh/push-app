package com.blackcrystalinfo.push.pusher.msg.impl;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.impl.GetuiMsgData;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.msg.AMsgPushService;

public class GetuiMsgPushService extends AMsgPushService {

	@Override
	protected void initPusher() {
		this.pusher = new GetuiMsgPusher();
	}

	@Override
	protected IData buildData(SendMessage msg) {
		GetuiMsgData data = new GetuiMsgData();

		return data;
	}

}
