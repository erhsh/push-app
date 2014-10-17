package com.blackcrystalinfo.push.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.collector.MsgCollector;
import com.blackcrystalinfo.push.exception.PushException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.parser.AlarmMsgParser;
import com.blackcrystalinfo.push.parser.IMsgParser;

public class PushService implements IService {

	private static final Logger logger = LoggerFactory
			.getLogger(PushService.class);

	private MsgCollector collector;

	private IMsgParser parser;

	private Map<MsgPushTypeEnum, APushService> serviceMap;

	private boolean isStart = false;

	public PushService() {
		collector = new MsgCollector();

		parser = new AlarmMsgParser();
		//
		serviceMap = new HashMap<MsgPushTypeEnum, APushService>();

		serviceMap.put(MsgPushTypeEnum.MSG, new MsgPushService("baidu"));
		serviceMap.put(MsgPushTypeEnum.SMS, new SmsPushService("haha"));

	}

	@Override
	public void startServcie() throws PushException {
		isStart = true;
		while (isStart) {
			// 1 collect
			MessageBean rawMsg = collector.receive();
			if (null == rawMsg) {
				continue;
			}
			logger.info("Received messageBean: {}", rawMsg);

			// 2 parse
			List<SendMessage> msgs = parser.parse(rawMsg);

			// 3 push
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
		}
	}

	@Override
	public void endService() {
		isStart = false;
		collector.close();
	}

}
