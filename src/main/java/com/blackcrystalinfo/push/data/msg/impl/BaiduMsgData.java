package com.blackcrystalinfo.push.data.msg.impl;

import com.blackcrystalinfo.push.data.msg.AMsgData;

public class BaiduMsgData extends AMsgData {
	private final Integer pushType = 1;

	private String userId = null;

	private Long channelId = null;

	private Integer deviceType = null;

	private Integer deployStatus = null;

	private Integer messageType = new Integer(0);

	private String message = null;

	private String msgKey = "channel_msg_key";

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

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(Integer deployStatus) {
		this.deployStatus = deployStatus;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
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

	public Integer getPushType() {
		return pushType;
	}

	@Override
	public String toString() {
		return "BaiduPushMessage [pushType=" + pushType + ", userId=" + userId
				+ ", channelId=" + channelId + ", deviceType=" + deviceType
				+ ", deployStatus=" + deployStatus + ", messageType="
				+ messageType + ", message=" + message + ", msgKey=" + msgKey
				+ "]";
	}

}
