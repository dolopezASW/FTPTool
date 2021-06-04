package com.asw.ftp.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asw.ftp.exception.FTPRuntimeException;
import com.asw.ftp.service.FTPService;
import com.asw.ftp.utils.FileFormatConverter;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class FTPServiceImpl implements FTPService {
	
	Logger logger = LoggerFactory.getLogger(FTPServiceImpl.class);
	
	/**
	 * Crea el canal de conexion con el servidor FTP
	 * 
	 * @return
	 * @throws FTPRuntimeException Si no puede crear el canal lanza una excepcion
	 */
	private ChannelSftp createChannelSftp(String user, String ftpServer, int port, String password) throws FTPRuntimeException {
		String nombreMetodo = "createChannelSftp()";

		try {
			JSch jSch = new JSch();
			logger.info("Creando sesion FTP {}", nombreMetodo);
			Session session = jSch.getSession(user, ftpServer, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect(60000);
			Channel channel = session.openChannel("sftp");
			channel.connect(60000);
			return (ChannelSftp) channel;
		} catch (JSchException ex) {
			logger.error("Error creando el canal sftp {}", nombreMetodo, ex);
			throw new FTPRuntimeException(ex.getLocalizedMessage());
		}
	}

	/**
	 * Cierra la conexion con el servidor FTP
	 * 
	 * @param channelSftp
	 * @throws Exception 
	 */
	private void disconnectChannelSftp(ChannelSftp channelSftp) throws Exception {
		String nombreMetodo = "disconnectChannelSftp()";
		try {
			logger.info("Cerrando canal sftp sftp {}", nombreMetodo);
			if (channelSftp == null)
				return;

			if (channelSftp.isConnected())
				channelSftp.disconnect();

			if (channelSftp.getSession() != null)
				channelSftp.getSession().disconnect();

		} catch (Exception ex) {
			logger.error("SFTP disconnect error {}", nombreMetodo, ex);
			throw new Exception();
		}
	}
	
	/**
	 * Devuelve el documento en forma de arreglo de bytes
	 * Si no encuentra el archivo o no puede conectarse con
	 * el servidor FTP, lanza una excepcion
	 * @throws Exception 
	 * @throws IOException
	 */
	@Override
	public byte[] getDocument(String filePath, String user, String password, String ftpServer, int port) throws Exception {

		String nombreMetodo = "getDocument()";

		String localFilePath = "/tmp/temp/";
		String remoteFilePath = filePath;
		ChannelSftp channelSftp = null;
		OutputStream outputStream;

		try {
			
			File file = new File(localFilePath);
			outputStream = new FileOutputStream(file);
		} catch (IOException ex) {
			throw new Exception();
		}

		try {
			channelSftp = createChannelSftp(user, ftpServer, port, password);
			logger.info("Obteniendo el archivo del servidor FTP {}", nombreMetodo);
			channelSftp.get(remoteFilePath, outputStream);
			InputStream stream = channelSftp.get(remoteFilePath);
			return FileFormatConverter.toBytes(stream);
		} catch (SftpException ex) {
			logger.error("No se encontró el archivo {}", nombreMetodo, ex);
			
			throw new Exception();
		} catch (FTPRuntimeException ex) {
			logger.error("Ha ocurrido un error con la conexion al servidor FTP {}", nombreMetodo, ex);
			
			throw new Exception();
		} catch (Exception ex) {
			logger.error("Ha ocurrido un error obteniendo el archivo firmado {}", nombreMetodo, ex);
			
			throw new Exception();
		} finally {
			outputStream.close();
			disconnectChannelSftp(channelSftp);
		}
	}

}
