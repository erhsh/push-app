package com.blackcrystalinfo.push.pusher.sms;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.sms.ASmsData;
import com.blackcrystalinfo.push.pusher.IPusher;

public abstract class ASmsPusher implements IPusher {
	@Override
	public void send(IData msg) {
		if (msg instanceof ASmsData) {
			ASmsData smsMsg = (ASmsData) msg;
			sms(smsMsg);
		}
	}

	protected abstract void sms(ASmsData smsMsg);
}
