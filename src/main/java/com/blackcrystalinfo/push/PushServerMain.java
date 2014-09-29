package com.blackcrystalinfo.push;

import com.blackcrystalinfo.push.collector.MsgCollector;

public class PushServerMain {

	public static void main(String[] args) {
		MsgCollector collector;
		try {
			collector = new MsgCollector();
			collector.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
