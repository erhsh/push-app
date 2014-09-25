package com.blackcrystalinfo.push.send.sms;

import com.blackcrystalinfo.push.msg.IMessage;
import com.blackcrystalinfo.push.msg.sms.ASmsMessage;
import com.blackcrystalinfo.push.send.IMsgSend;

public abstract class ASmsMsgSend implements IMsgSend {
	@Override
	public void send(IMessage msg) {
		if (msg instanceof ASmsMessage) {
			ASmsMessage smsMsg = (ASmsMessage) msg;
			sms(smsMsg);
		}
	}

	protected abstract void sms(ASmsMessage smsMsg);
}
