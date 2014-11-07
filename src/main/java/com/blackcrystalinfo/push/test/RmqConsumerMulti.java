package com.blackcrystalinfo.push.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RmqConsumerMulti {
	private static final String EXCHANGE_NAME = "MsgTopic";

	public static void main(String[] args) throws Exception {
		// 建立连接，得到channel：
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("www.erhsh.com");
	    factory.setPort(5672);
	    factory.setUsername("guest");
	    factory.setPassword("guest");


	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    // 构建消费者：
	    QueueingConsumer consumer = new QueueingConsumer(channel);

	    // 声明交换机，有则用，无则创建：
	    channel.exchangeDeclare("MsgTopic", "topic", true, false, false, null);

	    // 声明消息队列，有则用，无则创建：
	    channel.queueDeclare("queueAlarm", false, false, true, null);

	    // 队列绑定，指定该队列接收来自哪个交换机下哪条路由的消息：
	    channel.queueBind("queueAlarm", "MsgTopic", "rout1_1");
	    channel.queueBind("queueAlarm", "MsgTopic", "rout1_2");

	    // 指定消费者
	    channel.basicConsume("queueAlarm", true, consumer);

	    // 可绑定多个关系
	    channel.exchangeDeclare("MsgTopic", "topic", true, false, false, null);
	    channel.queueDeclare("updateQueue", false, false, true, null);
	    channel.queueBind("updateQueue", "MsgTopic", "#");
	    channel.queueBind("updateQueue", "MsgTopic", "rout2_2");
	    channel.basicConsume("updateQueue", true, consumer);

	    // 消费者消费消息
	    while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        String message = new String(delivery.getBody());
	        String routingKey = delivery.getEnvelope().getRoutingKey();

	        System.out.println(" [x] Received '" + routingKey + "':'" + message
	                + "'");
	    }
	}
}
