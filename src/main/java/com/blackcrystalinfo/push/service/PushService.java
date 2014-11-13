package com.blackcrystalinfo.push.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushException;
import com.blackcrystalinfo.push.receiver.IReceiver;
import com.blackcrystalinfo.push.receiver.impl.RmqReceiver;

public class PushService implements IService, PushServiceMBean {

	private static final Logger logger = LoggerFactory
			.getLogger(PushService.class);

	private IReceiver receiver;

	private boolean isStarted = false;

	public PushService() {
	}

	@Override
	public void startServcie() throws PushException {

		try {
			logger.info("----------------Starting Push Service----------------");

			receiver = new RmqReceiver();
			receiver.connect();
			receiver.receive();

			isStarted = true;

			runConnLoopChecker();

			logger.info("----------------Started Push Service ----------------");
		} catch (Exception e) {
			logger.error("Start Push Service error", e);
			throw new PushException(e);
		}

	}

	@Override
	public void endService() {
		logger.info("----------------Stopping Push Service----------------");
		isStarted = false;
		receiver.close();
		logger.info("----------------Stopped Push Service ----------------");
	}

	@Override
	public void doStart() {
		this.startServcie();
	}

	@Override
	public void doStop() {
		this.endService();
	}

	private void runConnLoopChecker() {
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
				logger.info("ConnLoopChecker exit.");
			}
		});
		mainLoopThread.setName("MainLoopThread");
		mainLoopThread.start();
	}

}
