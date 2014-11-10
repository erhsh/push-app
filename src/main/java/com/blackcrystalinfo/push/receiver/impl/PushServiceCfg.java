package com.blackcrystalinfo.push.receiver.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

/**
 * 推送服务配置文件映射类s<br>
 * 
 * 定义了某种推送类型下，使用的推送服务。
 * 
 * @author j
 * 
 */
public class PushServiceCfg {
	private static PushServiceCfg instance;

	public static PushServiceCfg getInst() {
		if (null != instance) {
			return instance;
		}

		return createRmqReceiveCfg();
	}

	private static synchronized PushServiceCfg createRmqReceiveCfg() {

		if (instance != null) {
			return instance;
		}

		InputStream is = ClassLoader
				.getSystemResourceAsStream("msg-push-cfg.xml");
		instance = JAXB.unmarshal(is, PushServiceCfg.class);
		return instance;
	}

	private List<PushService> pushService;

	public List<PushService> getPushService() {
		return pushService;
	}

	public void setPushService(List<PushService> pushService) {
		this.pushService = pushService;
	}

	@Override
	public String toString() {
		return "PushServiceCfg [pushService=" + pushService + "]";
	}

	static class PushService {
		private String pushType;
		private String defaultPusher;
		private Map<String, String> pusherMap;

		public String getPushType() {
			return pushType;
		}

		public void setPushType(String pushType) {
			this.pushType = pushType;
		}

		public String getDefaultPusher() {
			return defaultPusher;
		}

		public void setDefaultPusher(String defaultPusher) {
			this.defaultPusher = defaultPusher;
		}

		public Map<String, String> getPusherMap() {
			return pusherMap;
		}

		public void setPusherMap(Map<String, String> pusherMap) {
			this.pusherMap = pusherMap;
		}

		@Override
		public String toString() {
			return "PushService [pushType=" + pushType + ", defaultPusher="
					+ defaultPusher + ", pusherMap=" + pusherMap + "]";
		}

	}
}
