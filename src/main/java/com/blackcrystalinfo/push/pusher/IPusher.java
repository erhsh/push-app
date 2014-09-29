package com.blackcrystalinfo.push.pusher;

import com.blackcrystalinfo.push.data.IData;


/**
 * 发送消息给用户
 * @author j
 *
 */
public interface IPusher {
	void push(IData data);
}
