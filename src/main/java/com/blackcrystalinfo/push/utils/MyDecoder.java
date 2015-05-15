package com.blackcrystalinfo.push.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import sun.misc.BASE64Encoder;

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

			byte[] parseData = SmartHomeHead.subWebsocketHead(data);

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

			String mac = Long.toHexString(shd.srcID);
			System.out.println(mac);

			ByteBuffer bb = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			bb.putLong(shd.srcID);

			byte[] macBytes = bb.array();

			BASE64Encoder encoder = new BASE64Encoder();
			String str = encoder.encode(macBytes);
			System.out.println(str);
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
		new MyDecoder().decode("D:/tmp/alarmData/1417059114228");
		// new MyDecoder().decode("d:/rmq_data/new/5_1413362888330");
		// new MyDecoder().decode("d:/rmq_data/new/18");
		// new MyDecoder().decode("d:/rmq_data/new/20");
		// new MyDecoder().decode("d:/rmq_data/new/34");
	}
}
