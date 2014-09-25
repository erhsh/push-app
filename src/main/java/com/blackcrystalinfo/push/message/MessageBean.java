package com.blackcrystalinfo.push.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageBean {
	private String id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MessageBean [id=" + id + ", name=" + name + "]";
	}

	public static List<MessageBean> mock() {
		List<MessageBean> result = new ArrayList<MessageBean>();

		MessageBean mb1 = new MessageBean();
		mb1.setId("1");
		mb1.setName("Over Temperature");
		result.add(mb1);

		MessageBean mb2 = new MessageBean();
		mb2.setId("2");
		mb2.setName("Over Pressure");
		result.add(mb2);

		MessageBean mb3 = new MessageBean();
		mb3.setId("3");
		mb3.setName("Find Inbreak");
		result.add(mb3);

		MessageBean mb4 = new MessageBean();
		mb4.setId("4");
		mb4.setName("Change State");
		result.add(mb4);

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
