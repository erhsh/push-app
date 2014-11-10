package com.blackcrystalinfo.push.receiver.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushReceiverException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.parser.IMsgParser;
import com.blackcrystalinfo.push.pusher.IPushService;
import com.blackcrystalinfo.push.pusher.PushWorker;
import com.blackcrystalinfo.push.receiver.IReceiver;
import com.blackcrystalinfo.push.receiver.impl.PushServiceCfg.PushService;
import com.blackcrystalinfo.push.receiver.impl.RmqReceiveCfg.RecevieQueue;
import com.blackcrystalinfo.push.utils.Constants;
import com.blackcrystalinfo.push.utils.format.DateFormat;
import com.blackcrystalinfo.push.utils.reflect.ReflectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RmqReceiver implements IReceiver {
	private static final Logger logger = LoggerFactory
			.getLogger(RmqReceiver.class);

	private static final String RMQ_HOST = Constants.RMQ_HOST;
	private static final int RMQ_PORT = Integer.valueOf(Constants.RMQ_PORT);
	private static final String RMQ_USER = Constants.RMQ_USER;
	private static final String RMQ_PASS = Constants.RMQ_PASS;

	private ConnectionFactory factory;

	private Connection connection;

	private DateFormat format = new DateFormat();

	private Map<String, QueueingConsumer> consumerMap;

	private Map<String, IMsgParser> parserMap;

	private Map<MsgPushTypeEnum, IPushService> serviceMap;

	private ExecutorService executorService;

	public RmqReceiver() {
		this(RMQ_HOST, RMQ_PORT, RMQ_USER, RMQ_PASS);
	}

	public RmqReceiver(String host, int port, String username, String password) {
		logger.info("================Initialize RMQReceiver================");

		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);

		// 构造推送器
		logger.info("================init pusher...");
		serviceMap = new HashMap<MsgPushTypeEnum, IPushService>();
		for (PushService ps : PushServiceCfg.getInst().getPushService()) {
			String pushType = ps.getPushType();
			if (null == pushType || pushType.isEmpty()) {
				logger.warn("Push type is empty, ignore it.");
				continue;
			}

			String defaultPusher = ps.getDefaultPusher();
			if (null == defaultPusher || defaultPusher.isEmpty()) {
				logger.warn("Default pusher for {} is empty, ignore it.",
						pushType);
				continue;
			}

			Map<String, String> pusherMap = ps.getPusherMap();
			if (null == pusherMap || pusherMap.isEmpty()) {
				logger.warn("Pusher map is empty, ignore it.");
				continue;
			}

			String pusherClz = pusherMap.get(defaultPusher);
			if (null == pusherClz || pusherClz.isEmpty()) {
				logger.warn("Cannot find the default pusher, ignore it.");
				continue;
			}

			MsgPushTypeEnum pushTypeEnum = MsgPushTypeEnum.valueOf(pushType);
			IPushService pushService = ReflectionUtils.newInst(pusherClz);
			serviceMap.put(pushTypeEnum, pushService);
			logger.info("PushServiceCfg : [{}, {}]", pushTypeEnum, pushService);
		}

		logger.info("================init parser...");
		parserMap = new HashMap<String, IMsgParser>();
		for (RecevieQueue receiveQueue : RmqReceiveCfg.getInst()
				.getRecevieQueue()) {
			String queueName = receiveQueue.getQueueName();
			String msgParser = receiveQueue.getMsgParser();

			IMsgParser parser = ReflectionUtils.newInst(msgParser);
			parserMap.put(queueName, parser);
			logger.info("RmqReceiveCfg : [{}, {}]", queueName, parser);
		}

		executorService = Executors.newCachedThreadPool();
	}

	@Override
	public void connect() throws IOException {

		// 建立连接
		logger.info("Connect RMQ server: {}@{}:{}", factory.getUsername(),
				factory.getHost(), factory.getPort());
		connection = factory.newConnection();

		// 注册消息接收通道
		logger.info("Create channel...");
		Channel channel = connection.createChannel();

		// 路由绑定，明确一个queue关注来自哪个exchange的哪条routing信息
		logger.info("Create queues by RmqReceiveCfg...");
		consumerMap = new HashMap<String, QueueingConsumer>();
		for (RecevieQueue receiveQueue : RmqReceiveCfg.getInst()
				.getRecevieQueue()) {
			String queueName = receiveQueue.getQueueName();
			String exchgName = receiveQueue.getExchgName();

			// 队列 and 交换机
			logger.info("Receive queue declare [{}, {}]", queueName, exchgName);
			channel.queueDeclare(queueName, false, true, true, null);
			channel.exchangeDeclare(exchgName, "topic", true, false, false,
					null);

			// 队列绑定路由
			for (String routeKey : receiveQueue.getRouteKey()) {
				logger.info("Bind relation: ({}, {}->{})", queueName,
						exchgName, routeKey);
				channel.queueBind(queueName, exchgName, routeKey);
			}

			// 构造消费者
			logger.info("Create consumer...");
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			consumerMap.put(queueName, consumer);
		}

		logger.info("Connect success~~~");
	}

	@Override
	public void receive() {
		if (null == consumerMap) {
			throw new PushReceiverException("Please connect RMQ server first.");
		}

		for (Entry<String, QueueingConsumer> entry : consumerMap.entrySet()) {
			String queueName = entry.getKey();
			QueueingConsumer consumer = entry.getValue();
			ConsumerThread ct = new ConsumerThread(queueName, consumer);
			ct.setName("QueueConsumerThread-" + queueName);
			ct.setDaemon(true);
			ct.start();
		}
	}

	@Override
	public void close() {
		if (null != connection) {
			try {
				connection.close();
			} catch (IOException e) {
				logger.error("Close connection error!", e);
			}
		}

		executorService.shutdownNow();
	}

	public boolean isOpen() {

		return connection.isOpen();
	}

	class ConsumerThread extends Thread {

		private String queueName;
		private QueueingConsumer consumer;

		public ConsumerThread(String queueName, QueueingConsumer consumer) {
			this.queueName = queueName;
			this.consumer = consumer;
		}

		@Override
		public void run() {
			try {
				while (true) {

					Delivery delivery = consumer.nextDelivery();

					logger.info("Consumer get a delivery ^_^ ");
					Envelope envelop = delivery.getEnvelope();

					byte[] bs = delivery.getBody();

					MessageBean rawMsg = new MessageBean();
					rawMsg.setMsgId(envelop.getRoutingKey());
					rawMsg.setMsgData(bs);
					rawMsg.setDateTime(format.format(new Date()));

					PushWorker worker = new PushWorker(
							parserMap.get(queueName), serviceMap, rawMsg);
					executorService.execute(worker);

				}
			} catch (Exception e) {
				logger.warn("Receive data error!!! msg={}", e.getMessage());
			}

		}
	}

}
