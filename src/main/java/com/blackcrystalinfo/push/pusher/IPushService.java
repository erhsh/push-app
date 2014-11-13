package com.blackcrystalinfo.push.pusher;

import com.blackcrystalinfo.push.exception.PushPuserException;
import com.blackcrystalinfo.push.message.SendMessage;

public interface IPushService {
	void push(SendMessage msg) throws PushPuserException;
}
