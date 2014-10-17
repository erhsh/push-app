package com.blackcrystalinfo.push.utils;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUitls {
	public static boolean write(String pathname, byte[] data) {
		boolean result = false;
		
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(pathname));
			
			dos.write(data);
			dos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != dos){
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		FileUitls.write("d:/rmq_data/test.txt", "hello".getBytes());
	}
}
