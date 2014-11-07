package com.blackcrystalinfo.push.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushException;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.receiver.IReceiver;
import com.blackcrystalinfo.push.receiver.impl.RmqReceiver;

public class PushService implements IService, PushServiceMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(PushService.class);

	private IReceiver receiver;

	private Map<MsgPushTypeEnum, APushService> serviceMap;

	private boolean isStarted = false;

	public PushService() {
		receiver = new RmqReceiver();

		//
		serviceMap = new HashMap<MsgPushTypeEnum, APushService>();

		serviceMap.put(MsgPushTypeEnum.MSG, new MsgPushService("baidu"));
		serviceMap.put(MsgPushTypeEnum.SMS, new SmsPushService("haha"));

	}

	@Override
	public void startServcie() {

		logger.info("================Start Push Service===============");
		try {
			receiver.connect();
			receiver.receive();
		} catch (IOException e) {
			throw new PushException("Connect RMQ Server error!!!", e);
		}

		isStarted = true;
		Thread mainLoopThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isStarted) {
					// 每隔一段时间，检查连接是否断开
					RmqReceiver recv = (RmqReceiver) receiver;
					if (recv.isOpen()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
						}
						continue;
					}

					// 判断是否通过程序关闭连接
					if (!isStarted) {
						logger.info("Connection closed by manual~ ");
						return;
					}

					// 重连
					try {
						logger.info("Reconnect...");
						receiver.connect();
						receiver.receive();
					} catch (IOException e) {
						logger.error("Reconnect failed!!! msg={}",
								e.getMessage());
						// 等一会儿
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
						}
					}

				}
				logger.info("Push service exit.");
			}
		});
		mainLoopThread.setName("MainLoopThread");
		mainLoopThread.start();
	}

	@Override
	public void endService() {
		logger.info("================Stop Push Service===============");
		isStarted = false;
		receiver.close();
	}

	@Override
	public void doStart() {
		this.startServcie();
	}

	@Override
	public void doStop() {
		this.endService();
	}

}
