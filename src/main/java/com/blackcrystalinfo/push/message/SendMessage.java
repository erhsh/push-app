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
	private String id;

	private String title;

	private String content;

	private MsgPushTypeEnum type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "PushMessage [id=" + id + ", title=" + title + ", content="
				+ content + ", type=" + type + "]";
	}

	public static List<SendMessage> mock() {
		// 1=Title:Over Temperature; Content:template content here; NOTIFICATION
		// 2=Title:Over Pressure; Content:template content here; SMS
		// 3=Title:Find Inbreak, Content:template content here; ALL

		List<SendMessage> result = new ArrayList<SendMessage>();

		SendMessage sm1 = new SendMessage();
		sm1.setId("1");
		sm1.setTitle("Title:Over Temperature");
		sm1.setContent("Content:template content here");
		sm1.setType(MsgPushTypeEnum.valueOf("NOTIFICATION"));
		result.add(sm1);

		SendMessage sm2 = new SendMessage();
		sm2.setId("2");
		sm2.setTitle("Title:Over Pressure");
		sm2.setContent("Content:template content here");
		sm2.setType(MsgPushTypeEnum.valueOf("SMS"));
		result.add(sm2);

		SendMessage sm3 = new SendMessage();
		sm3.setId("3");
		sm3.setTitle("Title:Find Inbreak");
		sm3.setContent("Content:template content here");
		sm3.setType(MsgPushTypeEnum.valueOf("ALL"));
		result.add(sm3);

		return result;

	}
}
