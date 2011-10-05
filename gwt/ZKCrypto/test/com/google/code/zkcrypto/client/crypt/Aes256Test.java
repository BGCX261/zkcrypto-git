package com.google.code.zkcrypto.client.crypt;

import com.google.gwt.junit.client.GWTTestCase;

public class Aes256Test extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.google.code.zkcrypto.ZKCrypto";
	}

	public void testFIPS197() {
		testSingle("000102030405060708090a0b0c0d0e0f"
				+ "101112131415161718191a1b1c1d1e1f",
				"00112233445566778899aabbccddeeff",
				"8ea2b7ca516745bfeafc49904b496089");
	}

	public void testInconteam() {

		// Vectors from:
		// http://www.inconteam.com/software-development/41-encryption/55-aes-test-vectors#aes-ecb-256

		testSingle("603deb1015ca71be2b73aef0857d7781"
				+ "1f352c073b6108d72d9810a30914dff4",
				"6bc1bee22e409f96e93d7e117393172a",
				"f3eed1bdb5d2a03c064b5a7e3db181f8");

		testSingle("603deb1015ca71be2b73aef0857d7781"
				+ "1f352c073b6108d72d9810a30914dff4",
				"ae2d8a571e03ac9c9eb76fac45af8e51",
				"591ccb10d410ed26dc5ba74a31362870");

		testSingle("603deb1015ca71be2b73aef0857d7781"
				+ "1f352c073b6108d72d9810a30914dff4",
				"30c81c46a35ce411e5fbc1191a0a52ef",
				"b6ed21b99ca6f4f9f153e7b1beafed1d");

		testSingle("603deb1015ca71be2b73aef0857d7781"
				+ "1f352c073b6108d72d9810a30914dff4",
				"f69f2445df4f9b17ad2b417be66c3710",
				"23304b7a39f9f3ff067d8d8f9e24ecc7");
	}

	private static void assertArraysEqual(byte[] a1, byte[] a2) {
		assertEquals(a1.length, a2.length);
		for (int i = 0; i < a1.length; ++i)
			assertEquals(a1[i], a2[i]);
	}

	private void testSingle(byte[] key, byte[] plain, byte[] cipher) {
		assertNotNull(key);
		assertNotNull(plain);
		assertNotNull(cipher);

		assertEquals(key.length, Aes256.KEY_LENGTH);
		assertEquals(plain.length, Aes256.BLOCK_LENGTH);
		assertEquals(cipher.length, Aes256.BLOCK_LENGTH);

		Aes256 c = new Aes256(key);
		byte[] d = new byte[plain.length];
		System.arraycopy(plain, 0, d, 0, d.length);

		c.encrypt(d);
		assertArraysEqual(cipher, d);
		c.decrypt(d);
		assertArraysEqual(plain, d);
	}

	private static final String hexConv = "0123456789abcdef";

	private byte[] toarr(String s) {
		byte[] ret = new byte[s.length() / 2];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = (byte) (hexConv.indexOf(s.codePointAt(2 * i)) * 0x10);
			ret[i] += hexConv.indexOf(s.codePointAt(2 * i + 1));
		}
		return ret;
	}

	private void testSingle(String key, String plain, String cipher) {
		testSingle(toarr(key), toarr(plain), toarr(cipher));
	}

}
