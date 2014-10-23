package com.blackcrystalinfo.push.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for (int i = 0; i < 100; i++) {
			executorService.execute(new Runnable() {
				
				@Override
				public void run() {
					System.out
							.println("Test.main(...).new Runnable() {...}.run()");
				}
			});
		}
		
		System.out.println("Test.main()");
		
		Thread t = Thread.currentThread();
		
		t.setDaemon(true);
	}
}
