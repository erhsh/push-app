package com.blackcrystalinfo.push.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Animal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Animal [name123=" + name + "]";
	}
	
	public static void main(String[] args) {
		System.out.println("hello world");
	}
}

public class SerializeTest {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {

		Animal animal = new Animal();

		FileOutputStream fos = new FileOutputStream("d:/animal");

		ObjectOutputStream oos = new ObjectOutputStream(fos);

		animal.setName("Hello World");
		oos.writeObject(animal);
		oos.writeObject(new String("hello"));
		oos.close();

		FileInputStream fis = new FileInputStream("d:/animal");

		ObjectInputStream ois = new ObjectInputStream(fis);

		Object o = ois.readObject();
		Object r = ois.readObject();
		System.out.println(o);
		System.out.println(r);
		ois.close();
	}
}
