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

	private static List<AlarmUnit> aus;

	static {
		aus = new ArrayList<AlarmUnit>();

		aus.add(new AlarmUnit(0, "过压"));
		aus.add(new AlarmUnit(1, "过流"));
		aus.add(new AlarmUnit(2, "漏电"));
		aus.add(new AlarmUnit(3, "过温"));
		aus.add(new AlarmUnit(4, "气体"));
		aus.add(new AlarmUnit(5, "水侵"));
		aus.add(new AlarmUnit(6, "光线"));
		aus.add(new AlarmUnit(7, "红外"));
	}

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
				e.setTarget("763093826355642008");
				e.setTarget("120");
				e.setTitle("Data Abnormal");
				e.setContent("Parse code is: " + shdata.code + " --"
						+ rawMsg.getDateTime());
				e.setType(MsgPushTypeEnum.MSG);
				result.add(e);

				return result;
			}

			// alarm to who
			List<String> bindUsers = getBindUsers(data);

			// alarm what info
			String alarmInfo = getAlarmInfo(shdata);

			// which is alarm
			String alarmSrc = getAlarmSrc(data);

			for (String bindUser : bindUsers) {
				SendMessage sm = new SendMessage();
				sm.setTarget(bindUser);
				sm.setTitle(alarmInfo);
				sm.setContent(alarmSrc + "Time:" + rawMsg.getDateTime());
				sm.setType(MsgPushTypeEnum.MSG);
				result.add(sm);
			}

		}

		return result;
	}

	private List<String> getBindUsers(byte[] data) {
		List<String> result = new ArrayList<String>();

		// TODO: redis connect here
		result.add("120");

		return result;
	}

	private String getAlarmSrc(byte[] data) {
		// TODO Auto-generated method stub
		return "###Plug-14###";
	}

	private String getAlarmInfo(SmartHomeData shdata) {
		String result = "";
		byte[] data = shdata.data;

		if (null != data && data.length > 1) {
			result = getAlarmInfo(data[0]);
		}

		return result;
	}

	private String getAlarmInfo(byte b) {
		String result = "Alarm Info: ";

		for (AlarmUnit au : aus) {

			if ((b & au.getValue()) != 0) {
				result += au.getName() + " ";
			}
		}

		return result;
	}

	public static void main(String[] args) {
		String info = new AlarmMsgParser()
				.getAlarmInfo((byte) (1 | 4 | 8 | 128));

		System.out.println(info);
	}
}
