package com.asw.ftp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asw.ftp.service.FTPService;

@RestController
@RequestMapping("/ftp")
public class TestController {
	
	@Autowired
	private FTPService ftpService;
	
	@GetMapping("/getfile")
	public String test() throws Exception {
		ftpService.getDocument("/home/mrpotato/20201118/215292/495/632/contratos/Carta_de_Instrucciones-002.pdf", "firmas.sftp", "Alianza2021$", "192.168.115.73", 22);
		return "OK";
	}

}
