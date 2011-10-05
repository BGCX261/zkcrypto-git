package com.google.code.zkcrypto.client.crypt;

/**
 * Generic interface for hash algorithm functions
 * 
 * @author byo
 */
public interface HashAlgorithm {

	/**
	 * Get hash algorithm's name
	 * 
	 * @return algorithm's name
	 */
	String name();

	/**
	 * Get length of the computed hash
	 * 
	 * @return hash size in bytes
	 */
	int hashLength();

	/**
	 * Feed the hasher with arbitrary data
	 * 
	 * @param data
	 *            bytes used to compute the hash, not changed
	 */
	void feed(byte[] data);

	/**
	 * Finalize hashing and produce hash result
	 * 
	 * @return hash of data previously added through feed
	 */
	byte[] finish();
}
