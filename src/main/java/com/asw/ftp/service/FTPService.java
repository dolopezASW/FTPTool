package com.asw.ftp.service;

import org.springframework.stereotype.Service;

@Service
public interface FTPService {
	
	public byte[] getDocument(String filePath, String user, String password, String ftpServer, int port) throws Exception;

}
