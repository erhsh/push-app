package com.blackcrystalinfo.push.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushException;
import com.blackcrystalinfo.push.exception.PushReceiverException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.parser.AlarmMsgParser;
import com.blackcrystalinfo.push.parser.IMsgParser;
import com.blackcrystalinfo.push.receiver.IReceiver;
import com.blackcrystalinfo.push.receiver.impl.RmqReceiver;

public class PushService implements IService, PushServiceMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(PushService.class);

	private IReceiver receiver;

	private IMsgParser parser;

	private Map<MsgPushTypeEnum, APushService> serviceMap;

	private boolean isStart = false;

	private ExecutorService executorService;

	public PushService() {
		receiver = new RmqReceiver();

		parser = new AlarmMsgParser();
		//
		serviceMap = new HashMap<MsgPushTypeEnum, APushService>();

		serviceMap.put(MsgPushTypeEnum.MSG, new MsgPushService("baidu"));
		serviceMap.put(MsgPushTypeEnum.SMS, new SmsPushService("haha"));

		executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void startServcie() {

		logger.info("================Start Push Service===============");
		try {
			receiver.connect();
		} catch (IOException e) {
			throw new PushException("Connect RMQ Server error!!!", e);
		}

		isStart = true;
		Thread mainLoopThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isStart) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error("main looper interrupted.", e);
					}
				}
				logger.info("Push service exit.");
			}
		});
		mainLoopThread.setName("MainLoopThread");
		mainLoopThread.start();

		Thread receiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					// =============================== 接收数据
					MessageBean rawMsg = null;

					try {
						rawMsg = receiver.receive();
					} catch (PushReceiverException pre) {
						logger.warn("Receive data error!!! msg={}",
								pre.getMessage());

						if (!isStart) {
							// 手动关闭服务
							return;
						}

						// 重连
						try {
							logger.info("Reconnect...");
							receiver.connect();
						} catch (IOException e) {
							logger.error("Reconnect failed!!! msg={}",
									e.getMessage());
						}

						// 等一会儿
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
						}
					}

					// =============================== 非空判断
					if (null == rawMsg) {
						continue;
					}
					logger.info("Received messageBean: {}", rawMsg);

					// =============================== 异步推送
					PushWorker worker = new PushWorker(parser, serviceMap,
							rawMsg);
					executorService.execute(worker);

				}
			}
		});
		receiveThread.setName("ReceiveThread");
		receiveThread.setDaemon(true);
		receiveThread.start();
	}

	@Override
	public void endService() {
		logger.info("================Stop Push Service===============");
		isStart = false;
		receiver.close();
		executorService.shutdownNow();
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
