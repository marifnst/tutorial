package com.marifnst.tutorial.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class SampleChecksum {
	public void checkSum(String filePath, String checksumMethod) throws Exception {
		StringBuilder checkSumResult = new StringBuilder();

		try {
			// ====================== get checksum java
			// SHA, SHA-512, MD2, MD5, SHA-256, SHA-384...
			MessageDigest md = MessageDigest.getInstance(checksumMethod);
			File file = new File(filePath);
			try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), md)) {
				while (dis.read() != -1) ; //empty loop to clear the data
				md = dis.getMessageDigest();
			}
			// bytes to hex		
			for (byte b : md.digest()) {
				checkSumResult.append(String.format("%02x", b));
			}			
			System.out.println(checkSumResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}