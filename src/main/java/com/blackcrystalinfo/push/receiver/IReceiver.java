package com.blackcrystalinfo.push.receiver;

import java.io.IOException;

public interface IReceiver {
	public void connect() throws IOException;

	public void receive();

	public void close();
}
