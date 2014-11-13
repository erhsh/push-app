package com.blackcrystalinfo.push.pusher.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.exception.PushPuserException;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.IPushService;
import com.blackcrystalinfo.push.pusher.IPusher;

public abstract class AMsgPushService implements IPushService {

	private static final Logger logger = LoggerFactory
			.getLogger(AMsgPushService.class);
	protected IPusher pusher;

	public AMsgPushService() {
		initPusher();
	}

	@Override
	public void push(SendMessage msg) throws PushPuserException {
		if (null == pusher) {
			logger.warn("Please init msg puser first!!!");
			return;
		}

		IData data = buildData(msg);
		if (null == data) {
			logger.warn("Build push data is null!!!");
			return;
		}

		pusher.push(data);
	}

	protected abstract void initPusher();

	protected abstract IData buildData(SendMessage msg);

}
