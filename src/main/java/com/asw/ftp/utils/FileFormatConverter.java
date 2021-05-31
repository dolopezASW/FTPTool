package com.asw.ftp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileFormatConverter {
	
	/**
	 * Convierte InputStreamFile a byte[]
	 * Si no es posible convertir lanza una excepcion
	 * @param stream
	 * @return
	 * @throws Exception 
	 */
	public static byte[] toBytes(InputStream stream) throws Exception {
		
		byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    try {
			while ((bytesRead = stream.read(buffer)) != -1)
			{
			    output.write(buffer, 0, bytesRead);
			}
			return output.toByteArray();
		} catch (IOException e) {
			throw new Exception();
		}
	    
	}
	
	/**
	 * Convierte un Outputstream a InputStream
	 * @param outputStream
	 */
	public Object toBytes() {
		return null;
	}

}
