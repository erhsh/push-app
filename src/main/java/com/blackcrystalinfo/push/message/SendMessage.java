package com.blackcrystalinfo.push.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装消息推送结构，包含消息推送需要的所有信息实体类。
 * 
 * @author j
 * 
 */
public class SendMessage {

	/**
	 * 消息发送的目标信息来源。<br/>
	 * 可能是产生报警的设备，或者广告推送的目标用户，或者目标群体等。
	 */
	private String target;

	private String title;

	private String content;

	private MsgPushTypeEnum type;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MsgPushTypeEnum getType() {
		return type;
	}

	public void setType(MsgPushTypeEnum type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SendMessage [target=" + target + ", title=" + title
				+ ", content=" + content + ", type=" + type + "]";
	}

	public static List<SendMessage> mock() {
		List<SendMessage> result = new ArrayList<SendMessage>();

		SendMessage sm1 = new SendMessage();
		sm1.setTarget("dev001");
		sm1.setTitle("Title:Over Temperature");
		sm1.setContent("Content:template content here");
		sm1.setType(MsgPushTypeEnum.NOTIFICATION);
		result.add(sm1);

		SendMessage sm2 = new SendMessage();
		sm2.setTarget("dev002");
		sm2.setTitle("Title:Over Pressure");
		sm2.setContent("Content:template content here");
		sm2.setType(MsgPushTypeEnum.SMS);
		result.add(sm2);

		SendMessage sm3 = new SendMessage();
		sm3.setTarget("dev003");
		sm3.setTitle("Title:Find Inbreak");
		sm3.setContent("Content:template content here");
		sm3.setType(MsgPushTypeEnum.ALL);
		result.add(sm3);

		return result;

	}
	
	public static void main(String[] args) {
		for (SendMessage sm : mock()) {
			System.out.println(sm);
		}
	}
}
