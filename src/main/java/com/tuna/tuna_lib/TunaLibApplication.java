package com.tuna.tuna_lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TunaLibApplication {
//	private static long lastRefillTime = System.currentTimeMillis();

	public static void main(String[] args) {
		SpringApplication.run(TunaLibApplication.class, args);
	}

//	public static boolean tryAcquire() {
//		int capacity = 10;
//		int refillRate = 2;
//		int currentTokens = 0;
//
//		long elapsed = System.currentTimeMillis() - lastRefillTime;
//		if (elapsed > 0) {
//			int newTokens = (int) (elapsed * refillRate);
//			currentTokens = Math.min(capacity, newTokens);
//		}
//
//		if (currentTokens >= 1) {
//			currentTokens -= 1;
//			lastRefillTime = System.currentTimeMillis();
//			return true;
//		} else {
//			lastRefillTime = System.currentTimeMillis();
//			return false;
//		}
//	}

}
