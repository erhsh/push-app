package com.blackcrystalinfo.push.pusher.msg.impl;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData.MessageType;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData.PushType;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.msg.AMsgPushService;
import com.blackcrystalinfo.push.utils.Constants;

public class BaiduMsgPushService extends AMsgPushService {

	@Override
	protected void initPusher() {
		this.pusher = new BaiduMsgPusher();
	}

	@Override
	protected IData buildData(SendMessage msg) {
		BaiduMsgData baiduMsgData = new BaiduMsgData();
		baiduMsgData.setTag(msg.getTarget());
		baiduMsgData.setMessage("{\"title\":\"" + msg.getTitle()
				+ "\",\"description\":\"" + msg.getContent() + "\"}");
		baiduMsgData.setMessageType(MessageType.NOTICE);
		baiduMsgData.setPushType(PushType.TAG);
		baiduMsgData.setDeployStatus(new Integer(Constants.DEPLOY_STATUS));

		return baiduMsgData;
	}

}
