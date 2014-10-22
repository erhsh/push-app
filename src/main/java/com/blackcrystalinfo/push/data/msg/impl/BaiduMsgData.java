package com.blackcrystalinfo.push.data.msg.impl;

import com.blackcrystalinfo.push.data.msg.AMsgData;

public class BaiduMsgData extends AMsgData {

	/**
	 * 推送类型，取值范围为：1～3
	 * 
	 * @author j
	 * 
	 */
	public enum PushType {

		/**
		 * 1：单个人，必须指定user_id 和 channel_id （指定用户的指定设备）或者user_id（指定用户的所有设备）
		 */
		UNICAST(1),

		/**
		 * 2：一群人，必须指定 tag
		 */
		TAG(2),

		/**
		 * 3：所有人，无需指定tag、user_id、channel_id
		 */
		BROADCAST(3);

		private Integer value;

		private PushType(int value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 设备类型，取值范围为：1～5<br/>
	 * 云推送支持多种设备，默认为android设备类型。
	 * 
	 * @author j
	 * 
	 */
	public enum DeviceType {
		/**
		 * 1：浏览器设备；
		 */
		BROWSER(1),

		/**
		 * 2：PC设备；
		 */
		PC(2),

		/**
		 * 3：Android设备；
		 */
		ANDROID(3),

		/**
		 * 4：iOS设备；
		 */
		IOS(4),

		/**
		 * 5：Windows Phone设备；
		 */
		WIN_PHONE(5);

		private Integer value;

		private DeviceType(int value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 消息类型, 默认值为0。
	 * 
	 * @author j
	 * 
	 */
	public enum MessageType {
		/**
		 * 0：消息（透传给应用的消息体）
		 */
		MSG(0),

		/**
		 * 1：通知（对应设备上的消息通知）
		 */
		NOTICE(1);

		private Integer value;

		private MessageType(int value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	private String userId = null;

	private Long channelId = null;

	private String tag = null;

	private String message = null;

	private String msgKey = "channel_msg_key";

	private Integer deployStatus = null;

	private DeviceType deviceType = null;

	private MessageType messageType = MessageType.NOTICE;

	private PushType pushType = PushType.TAG;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public Integer getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(Integer deployStatus) {
		this.deployStatus = deployStatus;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public PushType getPushType() {
		return pushType;
	}

	public void setPushType(PushType pushType) {
		this.pushType = pushType;
	}

	@Override
	public String toString() {
		return "BaiduMsgData [userId=" + userId + ", channelId=" + channelId
				+ ", tag=" + tag + ", message=" + message + ", msgKey="
				+ msgKey + ", deployStatus=" + deployStatus + ", deviceType="
				+ deviceType + ", messageType=" + messageType + ", pushType="
				+ pushType + "]";
	}

}
