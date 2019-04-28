package com.marifnst.tutorial.samba;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class SampleSamba {

    private String sambaHost;
    private String sambaPort;
    private String sambaSharedFolder;
    private String sambaUser;
    private String sambaPassword;

	public void createFolderInSamba(String sambaFolderName) throws Exception {
		String sambaConnectionString = "smb://" + sambaHost + ":" + sambaPort + "/" + sambaSharedFolder + "/" + sambaFolderName;

		SmbFile smbFile = null;
		NtlmPasswordAuthentication auth = null;
		if (sambaUser.equals("") && sambaPassword.equals("")) {
			// smbFile = new SmbFile(sambaConnectionString);
			smbFile = new SmbFile(sambaConnectionString, NtlmPasswordAuthentication.ANONYMOUS);
		} else {
			auth = new NtlmPasswordAuthentication("", sambaUser, sambaPassword);
			smbFile = new SmbFile(sambaConnectionString, auth);
		}

		if (!smbFile.isDirectory()) {
			smbFile.mkdirs();
		} else {
//				System.out.println("folder available in samba");
		}
	}
	
	public void copyFileToSamba(String localFileName, String sambaFileName) throws Exception {
		String sambaConnectionString = "smb://" + sambaHost + ":" + "/" + sambaSharedFolder + "/" + sambaFileName;

		Path source = Paths.get(localFileName);
		SmbFile smbFile = null;
		NtlmPasswordAuthentication auth = null;

		if (sambaUser.equals("") && sambaPassword.equals("")) {
			// smbFile = new SmbFile(sambaConnectionString);
			smbFile = new SmbFile(sambaConnectionString, NtlmPasswordAuthentication.ANONYMOUS);
		} else {
			auth = new NtlmPasswordAuthentication("", sambaUser, sambaPassword);
			smbFile = new SmbFile(sambaConnectionString, auth);
		}

		OutputStream out = smbFile.getOutputStream();
		Files.copy(source, out);
	}

	public void moveSharedToSharedFile(String sourceShared, String destinationShared) throws Exception {
		String sourceConnectionString = "smb:" + sourceShared;
		String destinationConnectionString = "smb:" + destinationShared;

		SmbFile sourceFile = null;
		SmbFile destinationFile = null;
		NtlmPasswordAuthentication auth = null;

		if (sambaUser.equals("") && sambaPassword.equals("")) {
			// smbFile = new SmbFile(sambaConnectionString);
			sourceFile = new SmbFile(sourceConnectionString, NtlmPasswordAuthentication.ANONYMOUS);
			destinationFile = new SmbFile(destinationConnectionString, NtlmPasswordAuthentication.ANONYMOUS);
		} else {
			auth = new NtlmPasswordAuthentication("", sambaUser, sambaPassword);
			sourceFile = new SmbFile(sourceConnectionString, auth);
			destinationFile = new SmbFile(destinationConnectionString, auth);
		}

		if (destinationFile.exists()) {
			destinationFile.delete();
		}

		sourceFile.renameTo(destinationFile);
	}
}