package com.blackcrystalinfo.push.message;

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
}
