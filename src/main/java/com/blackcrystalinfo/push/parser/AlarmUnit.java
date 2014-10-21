package com.blackcrystalinfo.push.parser;

public class AlarmUnit {

	private String name;
	private byte value;

	public AlarmUnit(int index, String name) {
		this.value = (byte) (1 << index);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AlarmUnit [name=" + name + ", value=" + value + "]";
	}

	public static void main(String[] args) {
		AlarmUnit au1 = new AlarmUnit(10, "过温");
		System.out.println(au1);
	}
}
