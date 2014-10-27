package com.blackcrystalinfo.push.receiver.impl;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushReceiverException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.receiver.IReceiver;
import com.blackcrystalinfo.push.utils.Constants;
import com.blackcrystalinfo.push.utils.format.DateFormat;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RmqReceiver implements IReceiver {
	private static final Logger logger = LoggerFactory
			.getLogger(RmqReceiver.class);

	private static final String RMQ_HOST = Constants.RMQ_HOST;
	private static final int RMQ_PORT = Integer.valueOf(Constants.RMQ_PORT);
	private static final String RMQ_USER = Constants.RMQ_USER;
	private static final String RMQ_PASS = Constants.RMQ_PASS;

	private static final String RMQ_EXCHANGE_NAME = Constants.RMQ_EXCHANGE_NAME;

	private static final String RMQ_ROUTING_KEY = Constants.RMQ_ROUTING_KEY;

	private ConnectionFactory factory;
	private QueueingConsumer consumer;

	private Connection connection;

	private DateFormat format = new DateFormat();

	public RmqReceiver() {
		this(RMQ_HOST, RMQ_PORT, RMQ_USER, RMQ_PASS);
	}

	public RmqReceiver(String host, int port, String username, String password) {
		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
	}

	@Override
	public void connect() throws IOException {
		// 建立连接
		logger.info("Connect RMQ server: {}@{}:{}", factory.getUsername(),
				factory.getHost(), factory.getPort());
		connection = factory.newConnection();

		// 注册消息接收通道
		logger.info("Create channel: {}", RMQ_EXCHANGE_NAME);
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(RMQ_EXCHANGE_NAME, "topic", true, false, false,
				null);

		// 构造消费者
		logger.info("Bind consumer with routing key: {}", RMQ_ROUTING_KEY);
		consumer = new QueueingConsumer(channel);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, RMQ_EXCHANGE_NAME, RMQ_ROUTING_KEY);
		channel.basicConsume(queueName, true, consumer);

		logger.info("Connect success~~~");
	}

	@Override
	public MessageBean receive() {
		MessageBean result = null;

		QueueingConsumer.Delivery delivery;
		try {

			if (null == consumer) {
				throw new PushReceiverException(
						"Please connect RMQ server first.");
			}

			delivery = consumer.nextDelivery();

			String routingKey = delivery.getEnvelope().getRoutingKey();
			byte[] bs = delivery.getBody();

			result = new MessageBean();
			result.setMsgId(routingKey);
			result.setMsgData(bs);
			result.setDateTime(format.format(new Date()));
		} catch (ShutdownSignalException e) {
			throw new PushReceiverException("rmq down OR receiver stopped", e);
		} catch (ConsumerCancelledException e) {
			throw new PushReceiverException("consumer is cancelled", e);
		} catch (InterruptedException e) {
			throw new PushReceiverException("interrupted", e);
		}
		return result;
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
	}

}
