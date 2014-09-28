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
	public void send(IData msg) {
		if (msg instanceof AMsgData) {
			AMsgData pushMsg = (AMsgData) msg;
			push(pushMsg);
		}
	}

	protected abstract void push(AMsgData pushMsg);
}
