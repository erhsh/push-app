package com.blackcrystalinfo.push.receiver;

import java.io.IOException;

import com.blackcrystalinfo.push.message.MessageBean;

public interface IReceiver {
	public void connect() throws IOException;

	public MessageBean receive();

	public void close();
}
