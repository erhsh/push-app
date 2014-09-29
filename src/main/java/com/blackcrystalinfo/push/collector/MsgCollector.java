package com.blackcrystalinfo.push.collector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.ReceiveException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.AlarmMsgParser;
import com.blackcrystalinfo.push.parser.IMsgParser;
import com.blackcrystalinfo.push.service.APushService;
import com.blackcrystalinfo.push.service.MsgPushService;
import com.blackcrystalinfo.push.service.SmsPushService;
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

	private Map<MsgPushTypeEnum, APushService> serviceMap;

	private IMsgParser parser;

	public MsgCollector() {
		//
		serviceMap = new HashMap<MsgPushTypeEnum, APushService>();

		serviceMap.put(MsgPushTypeEnum.MSG, new MsgPushService("baidu"));
		serviceMap.put(MsgPushTypeEnum.SMS, new SmsPushService("haha"));

		parser = new AlarmMsgParser();
	}

	public void start() {
		logger.info("Start collect data from msg queue");

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
				logger.info("Receive data : " + mb);

				// 3.2. 解析消息
				SendMessage sm = parser.parse(mb);
				logger.info("Parse to: " + sm);

				// 3.3. 推送消息
				send(sm);

			}
		} catch (IOException e) {
			logger.error("Start collect error, msg : " + e.getMessage(), e);
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
			result.setMsgData(new String(bs));
		} catch (ShutdownSignalException e) {
			throw new ReceiveException("ShutdownSignalException", e);
		} catch (ConsumerCancelledException e) {
			throw new ReceiveException("ConsumerCancelledException", e);
		} catch (InterruptedException e) {
			throw new ReceiveException("InterruptedException", e);
		}

		return result;
	}

	private void send(SendMessage sm) {
		APushService service = serviceMap.get(sm.getType());

		if (null == service) {
			logger.warn("Unsurport MsgPushType : {}", sm.getType());
			return;
		}

		service.push(sm);

		logger.info("Send Ok~~~ \n");
	}

}
