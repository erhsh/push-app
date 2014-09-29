package com.blackcrystalinfo.push.service;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.pusher.IPusher;

public abstract class APushService {
	protected String providerName;

	protected IPusher pusher;

	protected IData data;

	public APushService(String providerName) {
		this.providerName = providerName;

		initProvider();
	}

	public void push(SendMessage msg) {
		initData(msg);

		pusher.push(data);
	}

	protected abstract void initProvider();

	protected abstract void initData(SendMessage msg);

}
