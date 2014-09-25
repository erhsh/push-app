package com.blackcrystalinfo.push;

import java.io.IOException;

public class PushServerMain {

	public static void main(String[] args) {
		MsgCollector collector;
		try {
			collector = new MsgCollector();
			collector.collect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
