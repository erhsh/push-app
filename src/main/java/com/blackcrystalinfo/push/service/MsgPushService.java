package com.blackcrystalinfo.push.service;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData.MessageType;
import com.blackcrystalinfo.push.data.msg.impl.BaiduMsgData.PushType;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.msg.impl.BaiduMsgPusher;
import com.blackcrystalinfo.push.pusher.msg.impl.GetuiMsgPusher;
import com.blackcrystalinfo.push.pusher.msg.impl.JiguangMsgPusher;
import com.blackcrystalinfo.push.utils.Constants;

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
	protected IData buildData(SendMessage msg) {

		if ("Baidu".equalsIgnoreCase(providerName)) {
			BaiduMsgData baiduMsgData = new BaiduMsgData();
			baiduMsgData.setTag(msg.getTarget());
			baiduMsgData.setMessage("{\"title\":\"" + msg.getTitle()
					+ "\",\"description\":\"" + msg.getContent() + "\"}");
			baiduMsgData.setMessageType(MessageType.NOTICE);
			baiduMsgData.setPushType(PushType.TAG);
			baiduMsgData.setDeployStatus(new Integer(Constants.DEPLOY_STATUS));

			return baiduMsgData;

		} else if ("Getui".equalsIgnoreCase(providerName)) {
			// TODO
		} else if ("Jiguang".equalsIgnoreCase(providerName)) {
			// TODO
		}

		return null;

	}

}
