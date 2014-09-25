package com.blackcrystalinfo.push;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

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

	private Map<String, SendMessage> msgSendCfg;

	private MsgSendService service;

	public MsgCollector() throws IOException {
		init();
	}

	private void init() throws IOException {

		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost(AMQ_ADDR);
		factory.setPort(AMQ_PORT);
		factory.setUsername(AMQ_USER);
		factory.setPassword(AMQ_PASS);

		connection = factory.newConnection();

		msgSendCfg = new HashMap<String, SendMessage>();
		for (SendMessage sm : SendMessage.mock()) {
			msgSendCfg.put(sm.getId(), sm);
		}

		service = new MsgSendService();
	}

	public void collect() {
		logger.info("start collect data from msg queue");
		try {

			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, false,
					null);
			String queueName = channel.queueDeclare().getQueue();

			String routingKey_reg = "4";
			channel.queueBind(queueName, EXCHANGE_NAME, routingKey_reg);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			int i = 0;
			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String routingKey = delivery.getEnvelope().getRoutingKey();

				byte[] bs = delivery.getBody();

				OutputStream os = new FileOutputStream("d:/rmq_data/"
						+ routingKey + i++, true);
				os.write(bs);
				os.close();

				String message = new String(delivery.getBody());

				logger.info(" [x] Received '" + routingKey + "':'" + message
						+ "'");

				// 2. Filter
				// SendMessage sendMsg = new SendMessage();
				// sendMsg.setId(routingKey);
				// sendMsg.setTitle(routingKey);
				// sendMsg.setContent(message);
				// sendMsg.setType(MsgPushTypeEnum.ALL);
				//
				// logger.info("SendMessage is: " + sendMsg);
				//
				// // 3. Send
				// service.send(sendMsg);
				// logger.info("Send Ok~~~");
			}

			// while (true) {
			// Thread.sleep(5000L);
			// System.out.println();
			//
			// // 1. Receive
			// MessageBean msgBean = receive();
			// if (null == msgBean) {
			// logger.error("receive nothing!");
			// continue;
			// } else {
			// logger.info("Received: " + msgBean);
			// }
			//
			// // 2. Filter
			// SendMessage pushMsg = filter(msgBean);
			// if (null == pushMsg) {
			// logger.warn("filter nothing!");
			// continue;
			// } else {
			// logger.info("Filtered: " + pushMsg);
			// }
			//
			// // 3. Send
			// service.send(pushMsg);
			// logger.info("Send Ok~~~");
			// }

		} catch (InterruptedException e) {
			logger.error("interrupted, e={}", e);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// private MessageBean receive() {
	// MessageBean result = null;
	//
	// // TODO: just mock
	// result = MessageBean.rand();
	//
	// return result;
	// }
	//
	// private SendMessage filter(MessageBean msgBean) {
	// SendMessage result = null;
	//
	// for (Entry<String, SendMessage> entry : msgSendCfg.entrySet()) {
	// if (entry.getKey().equals(msgBean.getId())) {
	// result = entry.getValue();
	// }
	// }
	//
	// return result;
	// }

}
