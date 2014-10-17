package com.blackcrystalinfo.push.utils.decoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class SmartHomeHead {
	private static int sequence = 0;

	public static SmartHomeData parseData(byte[] data, String passwd) {
		SmartHomeData shdata = new SmartHomeData();
		ByteBuffer headBB = ByteBuffer.allocate(data.length).order(
				ByteOrder.LITTLE_ENDIAN);
		if ((data[0] & 0x0a) != 0) {
			headBB.put(data, 0, 24);
			shdata.datID = headBB.getLong(8);
			shdata.srcID = headBB.getLong(16);
			shdata.opcode = 0x0a;
			shdata.code = 0;
			return shdata;
		}
		if (data.length < 24 + 12) {
			shdata.code = 4;
			return shdata;
		}
		headBB.put(data, 0, 24 + 12);
		short sequ = headBB.getShort(2);
		int ctime = headBB.getInt(4);
		shdata.datID = headBB.getLong(8);
		shdata.srcID = headBB.getLong(16);

		byte flags = headBB.get(24);
		shdata.isACK = ((flags >> 6) & 0x01) == 1 ? true : false;
		shdata.read = (byte) ((flags >> 7) & 0x01);
		shdata.devType = headBB.get(26);
		shdata.dadaSeuq = headBB.get(27);
		shdata.msgID = headBB.getShort(28);

		int length = headBB.getShort(30) & 0x0000ffff;
		short headcheck = headBB.getShort(32);
		short check = headBB.getShort(34);
		if (data.length - 24 - 12 < length) {
			shdata.code = 3;
			return shdata;
		}

		headBB.putInt(32, 0);

		if (headcheck != check(headBB.array())) {
			shdata.code = 1;
			return shdata;
		}
		ByteBuffer bb = ByteBuffer.allocate(length).order(
				ByteOrder.LITTLE_ENDIAN);
		bb.put(data, 24 + 12, length);

		byte el = (byte) ((flags >> 2) & 0x03);
		byte et = (byte) (flags & 0x03);

		data = Encryption.decryptData(bb.array(), el, et, ctime, sequ,
				shdata.srcID, shdata.srcID, passwd);
		if (check != check(data)) {
			shdata.code = 2;
			return shdata;
		}

		shdata.data = data;

		return shdata;

	}

	public static byte[] updateSequence(byte[] data, boolean isws) {
		ByteBuffer wsData = null;
		if (isws) {
			wsData = ByteBuffer.allocate(data.length);
			wsData.put(data, 0, 20);
			byte[] temp = data;
			data = new byte[data.length - 20];
			System.arraycopy(temp, 20, data, 0, data.length);
		}
		ByteBuffer bb = ByteBuffer.allocate(data.length).order(
				ByteOrder.LITTLE_ENDIAN);
		ByteBuffer headbb = ByteBuffer.allocate(24 + 12 - 2).order(
				ByteOrder.LITTLE_ENDIAN);
		headbb.put(data, 0, 24 + 12 - 4);
		headbb.putShort(2, (short) (sequence & 0x0000ffff));
		sequence++;
		short headCheck = check(headbb.array());
		headCheck = (short) ((headCheck >> 8) & 0xff + (headCheck << 8) & 0xff00);
		headbb.putShort(24 + 12 - 4, headCheck);

		bb.put(headbb.array());
		bb.put(data, 24 + 12 - 2, data.length - 24 - 12 + 2);
		if (isws) {
			wsData.put(bb.array());
			return wsData.array();
		}
		return bb.array();

	}

	public static byte[] addWebsocktHead(byte[] data, long srcID, long dstID) {
		ByteBuffer bb = ByteBuffer.allocate(data.length + 20);
		bb.putLong(dstID).putLong(srcID).putInt(0).put(data);
		return bb.array();
	}

	public static byte[] subWebsocketHead(byte[] wsData) {
		if (wsData.length < 20) {
			return new byte[1];
		}
		byte[] data = new byte[wsData.length - 20];
		System.arraycopy(wsData, 20, data, 0, data.length);
		return data;
	}

	public static short check(byte[] src) {
		if (src == null) {
			return 0;
		}
		byte[] temp = src;
		if (src.length % 2 != 0) {
			temp = new byte[src.length + 1];
			Arrays.fill(temp, (byte) 0);
			System.arraycopy(src, 0, temp, 0, src.length);
		}
		short result = 0;
		byte temp1 = 0, temp2 = 0;
		int length = src.length;
		for (int i = 0; i < length; i += 2) {
			try {
				temp1 ^= temp[i];
				temp2 ^= temp[i + 1];
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(e);
			}
		}
		result = temp1;
		result &= 0xff;
		result |= (short) (temp2 << 8);
		return result;
	}
}
