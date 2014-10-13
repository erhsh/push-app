package com.blackcrystalinfo.push.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.service.PushService;

/**
 * 服务启动操作类，操作服务的启动和关闭
 */
public class PushStartup {

	private static final Logger logger = LoggerFactory
			.getLogger(PushStartup.class);

	private PushService service;

	public void startUp() {

		try {
			service = new PushService();
			service.startServcie();

		} catch (Exception e) {
			logger.error("Start up push service error!!!", e);
			if (null != service) {
				service.endService();
			}
		}

	}

	public static void main(String[] args) {
		PushStartup main = new PushStartup();
		main.startUp();
	}

}
