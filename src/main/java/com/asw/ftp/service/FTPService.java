package com.asw.ftp.service;

public interface FTPService {
	
	public byte[] getDocument(String filePath, String user, String password, String ftpServer, int port) throws Exception;

}
