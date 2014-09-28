package com.blackcrystalinfo.push.collector;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.MsgSendService;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.AlarmMsgParser;
import com.blackcrystalinfo.push.parser.IMsgParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgCollector {
	private static final Logger logger = LoggerFactory
			.getLogger(MsgCollector.class);

	private Connection connection;

	private static final String AMQ_ADDR = "182.92.130.242";
	// private static final String AMQ_ADDR = "123.57.13.71";

	private static final int AMQ_PORT = 5672;
	private static final String AMQ_USER = "test";
	private static final String AMQ_PASS = "test123";

	private static final String EXCHANGE_NAME = "MsgTopic";

	private MsgSendService service;

	private IMsgParser parser;

	public MsgCollector() throws IOException {
		init();
	}

	private void init() throws IOException {
		//
		service = new MsgSendService();

		parser = new AlarmMsgParser();
	}

	public void collect() {
		logger.info("start collect data from msg queue");

		Connection connection = null;
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
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			
			// 3. 消费消息
			while (true) {
				
				// 3.1. 接收消息
				MessageBean mb = receive(consumer);
				if (null == mb) {
					continue;
				}
				
				// 3.2. 解析消息
				SendMessage sm = parser.parse(mb);
				logger.info("Parse to: " + sm);

				// 3.3. 推送消息
				service.send(sm);
				logger.info("Send Ok~~~");
			}
		} catch (IOException e) {
			logger.error("Collect Exception, e=" + e);
		} finally {
			if (null != connection) {
				try {
					connection.close();
				} catch (IOException e) {
					logger.error("Close connection exception, e=" + e);
				}
			}
		}

	}

	private MessageBean receive(QueueingConsumer consumer) {
		MessageBean result = null;

		QueueingConsumer.Delivery delivery;
		try {
			delivery = consumer.nextDelivery();

			String routingKey = delivery.getEnvelope().getRoutingKey();

			byte[] bs = delivery.getBody();

			result = new MessageBean();
			result.setMsgId(routingKey);
			result.setMsgData(bs);
			String message = new String(bs);
			logger.info(" [x] Received '" + routingKey + "':'" + message + "'");
		} catch (ShutdownSignalException e) {
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;
	}

}
