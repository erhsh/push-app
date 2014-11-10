package com.blackcrystalinfo.push.receiver.impl;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;

public class RmqReceiveCfg {

	private static RmqReceiveCfg instance;

	public static RmqReceiveCfg getInst() {
		if (null != instance) {
			return instance;
		}

		return createRmqReceiveCfg();
	}

	private static synchronized RmqReceiveCfg createRmqReceiveCfg() {

		if (instance != null) {
			return instance;
		}

		InputStream is = ClassLoader
				.getSystemResourceAsStream("msg-receive-cfg.xml");
		instance = JAXB.unmarshal(is, RmqReceiveCfg.class);
		return instance;
	}

	private List<RecevieQueue> recevieQueue;

	public List<RecevieQueue> getRecevieQueue() {
		return recevieQueue;
	}

	public void setRecevieQueue(List<RecevieQueue> recevieQueue) {
		this.recevieQueue = recevieQueue;
	}

	@Override
	public String toString() {
		return "RmqReceiveCfg [recevieQueue=" + recevieQueue + "]";
	}

	static class RecevieQueue {
		private String queueName;

		private String exchgName;

		private String msgParser;

		private List<String> routeKey;

		public String getQueueName() {
			return queueName;
		}

		public void setQueueName(String queueName) {
			this.queueName = queueName;
		}

		public String getExchgName() {
			return exchgName;
		}

		public void setExchgName(String exchgName) {
			this.exchgName = exchgName;
		}

		public String getMsgParser() {
			return msgParser;
		}

		public void setMsgParser(String msgParser) {
			this.msgParser = msgParser;
		}

		public List<String> getRouteKey() {
			return routeKey;
		}

		public void setRouteKey(List<String> routeKey) {
			this.routeKey = routeKey;
		}

		@Override
		public String toString() {
			return "RecevieQueue [queueName=" + queueName + ", exchgName="
					+ exchgName + ", msgParser=" + msgParser + ", routeKey="
					+ routeKey + "]";
		}

	}

}
