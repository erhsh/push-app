package com.blackcrystalinfo.push.receiver;

import java.io.IOException;

import com.blackcrystalinfo.push.exception.PushReceiverException;

public interface IReceiver {
	public void connect() throws IOException;

	public void receive() throws PushReceiverException;

	public void close();
}
