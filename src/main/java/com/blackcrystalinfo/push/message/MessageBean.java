package com.blackcrystalinfo.push.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageBean {
	private String msgId;

	private Object msgData;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Object getMsgData() {
		return msgData;
	}

	public void setMsgData(Object msgData) {
		this.msgData = msgData;
	}

	@Override
	public String toString() {
		return "MessageBean [msgId=" + msgId + ", msgData=" + msgData + "]";
	}

	public static List<MessageBean> mock() {
		List<MessageBean> result = new ArrayList<MessageBean>();

		MessageBean mb1 = new MessageBean();
		mb1.setMsgId("1");
		mb1.setMsgData("Status Changed Data");
		result.add(mb1);

		MessageBean mb2 = new MessageBean();
		mb2.setMsgId("2");
		mb2.setMsgData("Device Alarm Data");
		result.add(mb2);

		MessageBean mb3 = new MessageBean();
		mb3.setMsgId("3");
		mb3.setMsgData("Other Data");
		result.add(mb3);

		return result;
	}

	public static MessageBean rand() {
		MessageBean result = null;

		List<MessageBean> mbs = mock();

		Random random = new Random(System.currentTimeMillis());
		int randIndex = random.nextInt(mbs.size());

		result = mbs.get(randIndex);
		return result;
	}

	public static void main(String[] args) {
		System.out.println("========mock=========");
		for (MessageBean mb : mock()) {
			System.out.println(mb);
		}

		System.out.println("========rand=========");
		System.out.println(MessageBean.rand());
	}
}
