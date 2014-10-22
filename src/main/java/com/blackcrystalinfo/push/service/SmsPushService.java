package com.blackcrystalinfo.push.service;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.message.SendMessage;

public class SmsPushService extends APushService {

	public SmsPushService(String providerName) {
		super(providerName);
	}

	@Override
	protected void initProvider() {
		// TODO Auto-generated method stub

	}

	@Override
	protected IData buildData(SendMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
