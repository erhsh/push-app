package com.blackcrystalinfo.push.message;

import com.blackcrystalinfo.push.utils.StringUtils;

public class MessageBean {
	private String msgId;

	private byte[] msgData;

	private String dateTime;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public byte[] getMsgData() {
		return msgData;
	}

	public void setMsgData(byte[] msgData) {
		this.msgData = msgData;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "MessageBean [msgId=" + msgId + ", msgData="
				+ StringUtils.toHexString(msgData) + ", dateTime=" + dateTime
				+ "]";
	}

}
