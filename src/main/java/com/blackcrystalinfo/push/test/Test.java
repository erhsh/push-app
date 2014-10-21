package com.blackcrystalinfo.push.test;

import java.util.ArrayList;
import java.util.List;

import com.blackcrystalinfo.push.parser.AlarmUnit;

public class Test {

	public static List<AlarmUnit> aus = new ArrayList<AlarmUnit>();
	static {
		aus.add(new AlarmUnit(0, "过压"));
		aus.add(new AlarmUnit(1, "过流"));
		aus.add(new AlarmUnit(2, "漏电"));
		aus.add(new AlarmUnit(3, "过温"));
		aus.add(new AlarmUnit(4, "气体"));
		aus.add(new AlarmUnit(5, "水侵"));
		aus.add(new AlarmUnit(6, "光线"));
		aus.add(new AlarmUnit(7, "红外"));
	}

	public static void main(String[] args) {
//		byte[] bs = new byte[4];
		byte b = (byte) (8|3|16);
		
//		FileUitls.write("d:/tmp/byte", bs);
		
		System.out.println(parse(b));
		
		int a = 0xff;
		
		System.out.println(Integer.toBinaryString(a));
	}

	public static String parse(byte b) {
		String result = "";

		for (AlarmUnit au : aus) {

			if ((b & au.getValue()) != 0) {
				result += au.getName() + " ";
			}
		}

		return result;
	}
}
