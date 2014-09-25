package com.blackcrystalinfo.push.test;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RmqProducer {

	private final static String QUEUE_NAME = "MsgTopic";

	public static void main(String[] args) throws IOException {

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

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("MsgTopic", "myroutingkey", null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}
