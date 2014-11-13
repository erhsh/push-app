package com.blackcrystalinfo.push.pusher.sms;

import com.blackcrystalinfo.push.data.IData;
import com.blackcrystalinfo.push.data.sms.ASmsData;
import com.blackcrystalinfo.push.exception.PushPuserException;
import com.blackcrystalinfo.push.pusher.IPusher;

public abstract class ASmsPusher implements IPusher {
	@Override
	public void push(IData data) throws PushPuserException {
		if (data instanceof ASmsData) {
			ASmsData smsData = (ASmsData) data;
			pushSms(smsData);
		}
	}

	protected abstract void pushSms(ASmsData smsData) throws PushPuserException;
}
