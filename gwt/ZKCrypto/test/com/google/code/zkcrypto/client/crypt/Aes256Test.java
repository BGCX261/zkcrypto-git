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
		assertEquals(cipher, d);
		c.decrypt(d);
		assertEquals(plain, d);
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
