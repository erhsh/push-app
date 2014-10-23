package com.blackcrystalinfo.push.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import com.blackcrystalinfo.push.dao.DataHelper;
import com.blackcrystalinfo.push.exception.PushParserException;
import com.blackcrystalinfo.push.message.MessageBean;
import com.blackcrystalinfo.push.message.MsgPushTypeEnum;
import com.blackcrystalinfo.push.message.SendMessage;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeData;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeHead;
import com.blackcrystalinfo.push.utils.locale.Messages;

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

		aus.add(new AlarmUnit(0, Messages.locale("alarm.0.press")));
		aus.add(new AlarmUnit(1, Messages.locale("alarm.1.flow")));
		aus.add(new AlarmUnit(2, Messages.locale("alarm.2.power")));
		aus.add(new AlarmUnit(3, Messages.locale("alarm.3.temperature")));
		aus.add(new AlarmUnit(4, Messages.locale("alarm.4.gas")));
		aus.add(new AlarmUnit(5, Messages.locale("alarm.5.water")));
		aus.add(new AlarmUnit(6, Messages.locale("alarm.6.light")));
		aus.add(new AlarmUnit(7, Messages.locale("alarm.7.infrared")));
	}

	@Override
	public List<SendMessage> parse(MessageBean rawMsg) {
		List<SendMessage> result = new ArrayList<SendMessage>();

		// check msgData
		byte[] msgData = rawMsg.getMsgData();
		if (null == msgData) {
			throw new PushParserException("The msgData is empty!!!");
		}

		// decrypt msgData
		byte[] data = null;
		SmartHomeData shdata = null;
		try {
			data = SmartHomeHead.subWebsocketHead(msgData);
			shdata = SmartHomeHead.parseData(data, DATA_PARSE_PASS);
		} catch (Exception e) {
			throw new PushParserException("Decrypt msgData Failed!!!");
		}

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
		Set<String> bindUsers = getBindUsers(devId);

		for (String bindUser : bindUsers) {
			SendMessage sm = new SendMessage();
			sm.setTarget(bindUser);
			sm.setTitle(alarmInfo);
			sm.setContent(devName + " Time: " + rawMsg.getDateTime());
			sm.setTime(rawMsg.getDateTime());
			sm.setType(MsgPushTypeEnum.MSG);
			result.add(sm);
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

		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = DataHelper.getJedis();
			result = jedis.hget("device:mactoid", devMac);
		} catch (JedisException e) {
			isSuccess = false;
			DataHelper.returnBrokenJedis(jedis);
			throw e;
		} finally {
			if (isSuccess) {
				DataHelper.returnJedis(jedis);
			}
		}

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

		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = DataHelper.getJedis();
			result = jedis.hget("device:name", devId);
		} catch (JedisException e) {
			isSuccess = false;
			DataHelper.returnBrokenJedis(jedis);
			throw e;
		} finally {
			if (isSuccess) {
				DataHelper.returnJedis(jedis);
			}
		}

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
	private Set<String> getBindUsers(String devId) {
		Set<String> result = new HashSet<String>();

		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = DataHelper.getJedis();
			result = jedis.smembers("bind:device:" + devId);
		} catch (JedisException e) {
			isSuccess = false;
			DataHelper.returnBrokenJedis(jedis);
			throw e;
		} finally {
			if (isSuccess) {
				DataHelper.returnJedis(jedis);
			}
		}

		return result;
	}
}
