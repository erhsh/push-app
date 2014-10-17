package com.blackcrystalinfo.push.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeData;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeHead;

public class AlarmMsgParser implements IMsgParser {

	private static final Logger logger = LoggerFactory
			.getLogger(AlarmMsgParser.class);
	private static final String DATA_PARSE_PASS = "";

	@Override
	public List<SendMessage> parse(MessageBean rawMsg) {
		List<SendMessage> result = new ArrayList<SendMessage>();

		Object msgData = rawMsg.getMsgData();

		if (msgData instanceof byte[]) {
			byte[] wsData = (byte[]) msgData;

			byte[] data = SmartHomeHead.subWebsocketHead(wsData);
			SmartHomeData shdata = SmartHomeHead.parseData(data,
					DATA_PARSE_PASS);

			// parse abnormal
			if (0 != shdata.code) {

				logger.warn("Parse data abnormal, code={}", shdata.code);

				SendMessage e = new SendMessage();
				e.setTarget("xx");
				e.setTitle("xx");
				e.setContent("xx");
				e.setType(MsgPushTypeEnum.MSG);
				result.add(e);

				return result;
			}

			// alarm to who
			List<String> bindUsers = getBindUsers(data);

			// which is alarm
			String alarmSrc = getAlarmSrc(data);

			// alarm what info
			String alarmInfo = getAlarmInfo(shdata);

			for (String bindUser : bindUsers) {
				SendMessage sm = new SendMessage();
				sm.setTarget(bindUser);
				sm.setTitle(alarmInfo);
				sm.setContent(alarmSrc);
				sm.setType(MsgPushTypeEnum.MSG);
				result.add(sm);
			}

		}

		return result;
	}

	private List<String> getBindUsers(byte[] data) {
		List<String> result = new ArrayList<String>();

		// TODO: redis connect here
		result.add("160");

		return result;
	}

	private String getAlarmSrc(byte[] data) {
		// TODO Auto-generated method stub
		return "###Plug-14###";
	}

	private String getAlarmInfo(SmartHomeData shdata) {
		return "红外|光线|水浸|气体|过温|漏电|过流|过压";
	}
}
