package com.google.code.zkcrypto.client.crypt;

import com.google.gwt.junit.client.GWTTestCase;

public class Sha256Test extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.google.code.zkcrypto.ZKCrypto";
	}

	
	public void testHashes() {

		testSingle("", "e3b0c44298fc1c149afbf4c8996fb924"
				+ "27ae41e4649b934ca495991b7852b855");
		testSingle("*", "684888c0ebb17f374298b65ee2807526"
				+ "c066094c701bcc7ebbe1c1095f494fc1");
		testSingle("password", "5e884898da28047151d0e56f8dc62927"
				+ "73603d0d6aabbdd62a11ef721d1542d8");
		testSingle("684888c0ebb17f374298b65ee2807526"
				+ "c066094c701bcc7ebbe1c1095f494fc1",
				"664f7dcbf45d101e5a103190a60b3ec8"
						+ "ba961b1a29ec6b9963366a7437bc01f5");
		testSingle("684888c0ebb17f374298b65ee2807526"
				+ "c066094c701bcc7ebbe1c1095f494fc1"
				+ "664f7dcbf45d101e5a103190a60b3ec8"
				+ "ba961b1a29ec6b9963366a7437bc01f5"
				+ "b036458c8fe4e957baa6d8d05a7e936b"
				+ "4777f9bc7224738d86d6a15ad8696cf1"
				+ "f823dcafdf85670c91f5bcd51b62cefe"
				+ "dfb3fd4456c0f4f439134a7998145b97",
				"6aec876085bbc51d99878777a98d336d"
						+ "f8a9c81fa9344a74591c6d1e6cca1378");
		testSingle("The quick brown fox jumps over the lazy dog",
				"d7a8fbb307d7809469ca9abcb0082e4f"
						+ "8d5651e46d3cdb762d02d0bf37c9e592");
		testSingle("The quick brown fox jumps over the lazy dog.",
				"ef537f25c895bfa782526529a9b63d97"
						+ "aa631564d5d789c2b765448c8635fb6c");

		testSingle("abc", "ba7816bf8f01cfea414140de5dae2223"
				+ "b00361a396177a9cb410ff61f20015ad");

		testSingle("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
				"248d6a61d20638b8e5c026930c3e6039"
						+ "a33ce45964ff2167f6ecedd419db06c1");
	}

	private static final String hexConv = "0123456789abcdef";

	private void testSingle(String data, String expected) {

		Sha256 s = new Sha256();
		s.feed(data.getBytes());

		StringBuilder hash = new StringBuilder();
		for (byte b : s.finish()) {
			hash.append(hexConv.charAt((b >> 4) & 0xF));
			hash.append(hexConv.charAt((b >> 0) & 0xF));
		}

		assertEquals(expected, hash.toString());
	}
}
