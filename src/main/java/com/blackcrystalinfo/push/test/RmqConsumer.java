package com.blackcrystalinfo.push.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RmqConsumer {
	private static final String EXCHANGE_NAME = "MsgTopic";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("localhost");
		factory.setHost("193.168.1.201");
		factory.setHost("182.92.130.242");

		factory.setHost("123.57.13.71");
		factory.setPort(5672);
		factory.setUsername("test");
		factory.setPassword("test123");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, false,
				null);
		String queueName = channel.queueDeclare().getQueue();

		String routingKey_reg = "#";
		channel.queueBind(queueName, EXCHANGE_NAME, routingKey_reg);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String routingKey = delivery.getEnvelope().getRoutingKey();

			System.out.println(" [x] Received '" + routingKey + "':'" + message
					+ "'");
		}
	}
}
