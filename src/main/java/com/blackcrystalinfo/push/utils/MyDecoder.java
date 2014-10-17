package com.blackcrystalinfo.push.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.blackcrystalinfo.push.utils.decoder.SmartHomeData;
import com.blackcrystalinfo.push.utils.decoder.SmartHomeHead;

public class MyDecoder {

	public void decode(String pathname) {
		byte[] data = null;
		String passwd = "";
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream(pathname));
			int length = dis.available();
			data = new byte[length];
			dis.read(data);

			if (length < 20) {
				return;
			}

			byte[] parseData = new byte[length - 20];
			for (int i = 0; i < parseData.length; i++) {
				parseData[i] = data[i + 20];
			}

			SmartHomeData shd = SmartHomeHead.parseData(parseData, passwd);
			System.out.println("lenght: " + parseData.length);
			System.out.println("code: " + shd.code);
			System.out.println("devType: " + shd.devType);
			System.out.println("msgID: " + shd.msgID);
			System.out.println("ack: " + shd.isACK);

			System.out.println("srcId: " + shd.srcID);
			System.out.println("dstId: " + shd.dstID);
			System.out.println("mac: " + shd.mac);

			System.out.println("data: "
					+ (shd.data == null ? null : new String(shd.data)));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != dis) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		// new MyDecoder().decode("d:/rmq_data/new/3");
		// new MyDecoder().decode("d:/rmq_data/new/4_1413361594445");
		new MyDecoder().decode("d:/rmq_data/new/5_1413362888189");
		// new MyDecoder().decode("d:/rmq_data/new/5_1413362888330");
		// new MyDecoder().decode("d:/rmq_data/new/18");
		// new MyDecoder().decode("d:/rmq_data/new/20");
		// new MyDecoder().decode("d:/rmq_data/new/34");
	}
}
