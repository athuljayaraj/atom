package com.flytxt.atom.util;

public class TestUtil {

	public static void main(String[] args) {
		byte b = (byte) 0xDA;
		System.out.println(ByteArrayUtil.byteToBits(b));
//		b |= (1 << 6);
//		System.out.println(Compressor.byteToBits(b));
//		Random random = new Random();
//		long testLong = random.nextLong();
//		long testLong = 2;
//
//		if (testLong != (int) testLong) {
//			System.out.println("Long");
//		} else {
//			System.out.println("Int");
//		}

		for (int i = 0; i < 4; i++) {
//			int value = b & (3 << 6 - (2 * (i % 4)));
			int value = (b >> (2 * (3 - i))) & 3;
			System.out.println(value);
		}
	}
}
