package com.google.code.zkcrypto.client.crypt;

/**
 * Generic interface for symmetric ciphers
 * 
 * @author byo
 */
public interface SymmetricBlockCipher {

	/**
	 * Get cipher's name
	 * 
	 * @return cipher's name
	 */
	String name();

	/**
	 * Get size of cipher's block
	 * 
	 * @return block size in bytes
	 */
	int blockSize();

	/**
	 * Encode one data block
	 * 
	 * @param data data block (input and output)
	 */
	void encrypt(byte[] data);

	/**
	 * Decode one data block
	 * 
	 * @param data data block (input and output)
	 */
	void decrypt(byte[] data);
}
