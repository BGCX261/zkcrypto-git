package com.google.code.zkcrypto.client.crypt;

/**
 * Class that performs AES-256 Encryption
 * 
 * This class, similarly to SHA-256 was created in order to work in any
 * java-based environment including the GWT one. Because of that fact, it may
 * not be as fast as possible since we need to consider all GWT's limits.
 * 
 * Based on: http://www.literatecode.com/2007/11/11/aes256/
 * 
 * @author byo
 * 
 */
public class Aes256 {

	public static final int KEY_LENGTH = 32;
	public static final int BLOCK_LENGTH = 16;

	private static final int[] Sbox = { 0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B,
			0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76, 0xCA,
			0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF,
			0x9C, 0xA4, 0x72, 0xC0, 0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7,
			0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15, 0x04, 0xC7,
			0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB,
			0x27, 0xB2, 0x75, 0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0,
			0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84, 0x53, 0xD1, 0x00,
			0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C,
			0x58, 0xCF, 0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45,
			0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8, 0x51, 0xA3, 0x40, 0x8F,
			0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3,
			0xD2, 0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7,
			0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73, 0x60, 0x81, 0x4F, 0xDC, 0x22,
			0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
			0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC,
			0x62, 0x91, 0x95, 0xE4, 0x79, 0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5,
			0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08, 0xBA,
			0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F,
			0x4B, 0xBD, 0x8B, 0x8A, 0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6,
			0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E, 0xE1, 0xF8,
			0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE,
			0x55, 0x28, 0xDF, 0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68,
			0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16 };

	private static final int[] SboxInv = { 0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36,
			0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB, 0x7C,
			0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44,
			0xC4, 0xDE, 0xE9, 0xCB, 0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23,
			0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E, 0x08, 0x2E,
			0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D,
			0x8B, 0xD1, 0x25, 0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16,
			0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92, 0x6C, 0x70, 0x48,
			0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D,
			0x9D, 0x84, 0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7,
			0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06, 0xD0, 0x2C, 0x1E, 0x8F,
			0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A,
			0x6B, 0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2,
			0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73, 0x96, 0xAC, 0x74, 0x22, 0xE7,
			0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
			0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62,
			0x0E, 0xAA, 0x18, 0xBE, 0x1B, 0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2,
			0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4, 0x1F,
			0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59,
			0x27, 0x80, 0xEC, 0x5F, 0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A,
			0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF, 0xA0, 0xE0,
			0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83,
			0x53, 0x99, 0x61, 0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26,
			0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D };

	private static byte xtime(byte x) {
		return (byte) ((x & 0x80) != 0 ? ((x << 1) ^ 0x1B) : (x << 1));
	}

	private static void subBytes(byte[] buff) {
		for (int i = 16; i-- > 0;)
			buff[i] = (byte) Sbox[buff[i] & 0xFF];
	}

	private static void subBytesInv(byte[] buff) {
		for (int i = 16; i-- > 0;)
			buff[i] = (byte) SboxInv[buff[i] & 0xFF];
	}

	private static void addRoundKey(byte[] buff, byte[] key) {
		for (int i = 16; i-- > 0;)
			buff[i] ^= key[i];
	}

	private static void addRoundKey2(byte[] buff, byte[] key) {
		for (int i = 16; i-- > 0;)
			buff[i] ^= key[16 + i];
	}

	private static void addRoundKeyCpy(byte[] buff, byte[] key, byte[] cpk) {
		for (int i = 16; i-- > 0;) {
			buff[i] ^= key[i];
			cpk[i] = key[i];
			cpk[i + 16] = key[i + 16];
		}
	}

	private static void shiftRows(byte[] buf) {
		byte i;

		i = buf[1];
		buf[1] = buf[5];
		buf[5] = buf[9];
		buf[9] = buf[13];
		buf[13] = i;

		i = buf[10];
		buf[10] = buf[2];
		buf[2] = i;

		i = buf[3];
		buf[3] = buf[15];
		buf[15] = buf[11];
		buf[11] = buf[7];
		buf[7] = i;

		i = buf[14];
		buf[14] = buf[6];
		buf[6] = i;
	}

	private static void shiftRowsInv(byte[] buf) {

		byte i;

		i = buf[1];
		buf[1] = buf[13];
		buf[13] = buf[9];
		buf[9] = buf[5];
		buf[5] = i;

		i = buf[2];
		buf[2] = buf[10];
		buf[10] = i;

		i = buf[3];
		buf[3] = buf[7];
		buf[7] = buf[11];
		buf[11] = buf[15];
		buf[15] = i;

		i = buf[6];
		buf[6] = buf[14];
		buf[14] = i;
	}

	private static void mixColumns(byte[] buf) {
		for (int i = 0; i < 16; i += 4) {
			byte a = buf[i + 0];
			byte b = buf[i + 1];
			byte c = buf[i + 2];
			byte d = buf[i + 3];
			byte e = (byte) (a ^ b ^ c ^ d);
			buf[i + 0] ^= e ^ xtime((byte) (a ^ b));
			buf[i + 1] ^= e ^ xtime((byte) (b ^ c));
			buf[i + 2] ^= e ^ xtime((byte) (c ^ d));
			buf[i + 3] ^= e ^ xtime((byte) (d ^ a));
		}
	}

	private static void mixColumnsInv(byte[] buf) {
		for (int i = 0; i < 16; i += 4) {
			byte a = buf[i + 0];
			byte b = buf[i + 1];
			byte c = buf[i + 2];
			byte d = buf[i + 3];
			byte e = (byte) (a ^ b ^ c ^ d);
			byte z = xtime(e);
			byte x = (byte) (e ^ xtime(xtime((byte) (z ^ a ^ c))));
			byte y = (byte) (e ^ xtime(xtime((byte) (z ^ b ^ d))));
			buf[i + 0] ^= x ^ xtime((byte) (a ^ b));
			buf[i + 1] ^= y ^ xtime((byte) (b ^ c));
			buf[i + 2] ^= x ^ xtime((byte) (c ^ d));
			buf[i + 3] ^= y ^ xtime((byte) (d ^ a));
		}
	}

	private static byte F(byte x) {
		return (byte) ((x << 1) ^ (((x >> 7) & 1) * 0x1b));
	}

	private static byte FD(byte x) {
		return (byte) ((x >> 1) ^ ((x & 1) * 0x8d));
	}

	private static void expandEncKey(byte[] k, Byte rc) {
		k[0] ^= Sbox[k[29] & 0xFF] ^ rc;
		k[1] ^= Sbox[k[30] & 0xFF];
		k[2] ^= Sbox[k[31] & 0xFF];
		k[3] ^= Sbox[k[28] & 0xFF];
		rc = F(rc);

		for (int i = 4; i < 16; i += 4) {
			k[i + 0] ^= k[i - 4];
			k[i + 1] ^= k[i - 3];
			k[i + 2] ^= k[i - 2];
			k[i + 3] ^= k[i - 1];
		}
		k[16] ^= Sbox[k[12] & 0xFF];
		k[17] ^= Sbox[k[13] & 0xFF];
		k[18] ^= Sbox[k[14] & 0xFF];
		k[19] ^= Sbox[k[15] & 0xFF];

		for (int i = 20; i < 32; i += 4) {
			k[i + 0] ^= k[i - 4];
			k[i + 1] ^= k[i - 3];
			k[i + 2] ^= k[i - 2];
			k[i + 3] ^= k[i - 1];
		}

	}

	private static void expandDecKey(byte[] k, Byte rc) {

		for (int i = 28; i > 16; i -= 4) {
			k[i + 0] ^= k[i - 4];
			k[i + 1] ^= k[i - 3];
			k[i + 2] ^= k[i - 2];
			k[i + 3] ^= k[i - 1];
		}

		k[16] ^= Sbox[k[12] & 0xFF];
		k[17] ^= Sbox[k[13] & 0xFF];
		k[18] ^= Sbox[k[14] & 0xFF];
		k[19] ^= Sbox[k[15] & 0xFF];

		for (int i = 12; i > 0; i -= 4) {
			k[i + 0] ^= k[i - 4];
			k[i + 1] ^= k[i - 3];
			k[i + 2] ^= k[i - 2];
			k[i + 3] ^= k[i - 1];
		}

		rc = FD(rc);
		k[0] ^= Sbox[k[29] & 0xFF] ^ rc;
		k[1] ^= Sbox[k[30] & 0xFF];
		k[2] ^= Sbox[k[31] & 0xFF];
		k[3] ^= Sbox[k[28] & 0xFF];
	}

	private byte[] enckey;
	private byte[] deckey;

	public Aes256(byte[] key) {

		assert key != null;
		assert key.length == KEY_LENGTH;

		Byte rcon = 1;
		enckey = new byte[KEY_LENGTH];
		deckey = new byte[KEY_LENGTH];

		for (int i = 0; i < KEY_LENGTH; i++)
			enckey[i] = deckey[i] = key[i];
		for (int i = 1; i < 8; ++i)
			expandEncKey(deckey, rcon);
	}

	public void encrypt(byte[] buf) {

		assert buf != null;
		assert buf.length == BLOCK_LENGTH;

		Byte rcon = 1;
		byte[] key = new byte[KEY_LENGTH];

		addRoundKeyCpy(buf, enckey, key);
		for (int i = 1; i < 14; ++i) {
			subBytes(buf);
			shiftRows(buf);
			mixColumns(buf);
			if ((i & 1) != 0) {
				addRoundKey2(buf, key);
			} else {
				expandEncKey(key, rcon);
				addRoundKey(buf, key);
			}
		}
		subBytes(buf);
		shiftRows(buf);
		expandEncKey(key, rcon);
		addRoundKey(buf, key);
	}

	public void decrypt(byte[] buf) {

		assert buf != null;
		assert buf.length == BLOCK_LENGTH;

		Byte rcon = (byte) 0x80;
		byte[] key = new byte[KEY_LENGTH];

		addRoundKeyCpy(buf, deckey, key);
		shiftRowsInv(buf);
		subBytesInv(buf);

		for (int i = 14; --i > 0;) {
			if ((i & 1) != 0) {
				expandDecKey(key, rcon);
				addRoundKey2(buf, key);
			} else {
				addRoundKey(buf, key);
			}
			mixColumnsInv(buf);
			shiftRowsInv(buf);
			subBytesInv(buf);
		}
		addRoundKey(buf, key);
	}

}