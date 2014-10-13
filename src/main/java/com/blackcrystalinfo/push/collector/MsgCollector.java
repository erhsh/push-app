package com.blackcrystalinfo.push.collector;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.ReceiveException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgCollector {
	private static final Logger logger = LoggerFactory
			.getLogger(MsgCollector.class);

	// private static final String AMQ_ADDR = "182.92.130.242";
	private static final String AMQ_ADDR = "123.57.13.71";

	private static final int AMQ_PORT = 5672;
	private static final String AMQ_USER = "test";
	private static final String AMQ_PASS = "test123";

	private static final String EXCHANGE_NAME = "MsgTopic";

	private QueueingConsumer consumer;

	private Connection connection;

	public MsgCollector() {
		start();
	}

	public void start() {
		try {
			// 1. 建立连接
			ConnectionFactory factory = new ConnectionFactory();

			factory.setHost(AMQ_ADDR);
			factory.setPort(AMQ_PORT);
			factory.setUsername(AMQ_USER);
			factory.setPassword(AMQ_PASS);

			connection = factory.newConnection();

			// 2. 注册消息
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, false,
					null);
			String queueName = channel.queueDeclare().getQueue();
			String routingKey_reg = "4";
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey_reg);
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

		} catch (IOException e) {
			logger.error("Start collect error!!! msg = {}", e.getMessage());
			throw new ReceiveException("Start collect error", e);
		}

	}

	public MessageBean receive() {
		MessageBean result = null;

		QueueingConsumer.Delivery delivery;
		try {
			if (null == consumer) {
				logger.error("Rabbit MQ's consumer is null");
				Thread.sleep(1000);
				return result;
			}

			delivery = consumer.nextDelivery();

			String routingKey = delivery.getEnvelope().getRoutingKey();
			byte[] bs = delivery.getBody();

			result = new MessageBean();
			result.setMsgId(routingKey);
			result.setMsgData(new String(bs));
		} catch (ShutdownSignalException e) {
			logger.error("Shutdown Signal Exception", e);
		} catch (ConsumerCancelledException e) {
			logger.error("Consumer Cancelled Exception", e);
		} catch (InterruptedException e) {
			logger.error("Interrupted Exception", e);
		}

		return result;
	}

	public void close() {
		if (null != connection) {
			try {
				connection.close();
			} catch (IOException e) {
				logger.error("Close connection exception, e=" + e);
			}
		}
	}
}
