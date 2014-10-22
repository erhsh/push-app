package com.blackcrystalinfo.push.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.blackcrystalinfo.push.dao.DataHelper;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeData;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeHead;

/**
 * 报警消息解析器
 * 
 * @author j
 * 
 */
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

		byte[] msgData = rawMsg.getMsgData();

		if (null != msgData) {
			byte[] data = SmartHomeHead.subWebsocketHead(msgData);
			SmartHomeData shdata = SmartHomeHead.parseData(data,
					DATA_PARSE_PASS);

			// parse abnormal
			if (0 != shdata.code) {

				logger.warn("Parse data abnormal, code={}", shdata.code);

				SendMessage e = new SendMessage();
				e.setTarget("120");
				e.setTitle("Data Abnormal");
				e.setContent("Parse code is: " + shdata.code + " --"
						+ rawMsg.getDateTime());
				e.setType(MsgPushTypeEnum.MSG);
				result.add(e);

				return result;
			}

			// get device id
			String devId = parseDevId(data);
			if (null == devId || "".equals(devId)) {
				String devMac = String.valueOf(shdata.mac);
				devId = getDevId(devMac);
			}

			// which is alarm
			String devName = getDevName(devId);

			// alarm what info
			String alarmInfo = "";
			byte[] alarmData = shdata.data;
			if (null != data && data.length > 1) {
				alarmInfo = getAlarmInfo(alarmData[0]);
			}

			// alarm to who
			List<String> bindUsers = getBindUsers(devId);

			for (String bindUser : bindUsers) {
				SendMessage sm = new SendMessage();
				sm.setTarget(bindUser);
				sm.setTitle(alarmInfo);
				sm.setContent(devName + " Time: " + rawMsg.getDateTime());
				sm.setTime(rawMsg.getDateTime());
				sm.setType(MsgPushTypeEnum.MSG);
				result.add(sm);
			}

		}

		return result;
	}

	/**
	 * 解析设备Id
	 * 
	 * @param data
	 *            报警数据
	 * @return 报警设备id
	 */
	private String parseDevId(byte[] data) {

		String result = "";

		// TODO: 从前20字节里取？

		return result;
	}

	/**
	 * 根据mac地址，查找设备id
	 * 
	 * @param devMac
	 *            设备mac
	 * @return 设备id
	 */
	private String getDevId(String devMac) {
		String result = "";

		// TODO: find id by mac
		Jedis jedis = DataHelper.getJedis();
		result = jedis.hget("device:mactoid", devMac);

		return result;
	}

	/**
	 * 获取报警设备的名称
	 * 
	 * @param devId
	 *            设备id
	 * @return 设备名称
	 */
	private String getDevName(String devId) {
		String result = "";

		// TODO: find name by id
		Jedis jedis = DataHelper.getJedis();
		result = jedis.hget("device:idtoname", devId);

		return result;
	}

	/**
	 * 解析报警数据
	 * 
	 * @param b
	 *            标志报警类型的一个字节
	 * @return 组合的报警信息
	 */
	private String getAlarmInfo(byte b) {
		String result = "Alarm Info: ";

		for (AlarmUnit au : aus) {
			if ((b & au.getValue()) != 0) {
				result += au.getName() + " ";
			}
		}

		return result;
	}

	/**
	 * 获取绑定用户列表
	 * 
	 * @param devId
	 *            报警设备id
	 * @return 绑定用户列表
	 */
	private List<String> getBindUsers(String devId) {
		List<String> result = new ArrayList<String>();

		// TODO: find binded users
		result.add("120");
		return result;
	}
}
