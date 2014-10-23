package com.blackcrystalinfo.push.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.exception.PushException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.IMsgParser;

public class PushWorker implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(PushWorker.class);

	private IMsgParser parser;

	private Map<MsgPushTypeEnum, APushService> serviceMap;

	private MessageBean rawMsg;

	public PushWorker(IMsgParser parser,
			Map<MsgPushTypeEnum, APushService> serviceMap, MessageBean rawMsg) {

		this.parser = parser;

		this.serviceMap = serviceMap;

		this.rawMsg = rawMsg;
	}

	@Override
	public void run() {

		try {
			// parse
			List<SendMessage> msgs = parser.parse(rawMsg);

			if (null == msgs || msgs.isEmpty()) {
				logger.warn("There is no binded user, rawMsg = {}", rawMsg);
				return;
			}

			// push
			for (SendMessage msg : msgs) {
				APushService pushService = serviceMap.get(msg.getType());
				if (null == pushService) {
					if (MsgPushTypeEnum.ALL == msg.getType()) {
						for (Entry<MsgPushTypeEnum, APushService> entry : serviceMap
								.entrySet()) {
							entry.getValue().push(msg);
						}
					}
				} else {
					pushService.push(msg);
				}
			}
		} catch (PushException e) {
			logger.error("Failed to push rawMsg = " + rawMsg, e);
		}

	}

}
