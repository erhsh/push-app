package com.blackcrystalinfo.push.pusher.msg;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.msg.AMsgData;
import com.blackcrystalinfo.push.pusher.IPusher;

/**
 * 发送推送消息给用户
 * 
 * @author j
 * 
 */
public abstract class AMsgPusher implements IPusher {

	@Override
	public void push(IData data) {
		if (data instanceof AMsgData) {
			AMsgData msgData = (AMsgData) data;
			pushMsg(msgData);
		}
	}

	protected abstract void pushMsg(AMsgData msgData);
}
