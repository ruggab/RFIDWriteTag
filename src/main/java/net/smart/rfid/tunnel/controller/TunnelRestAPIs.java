package net.smart.rfid.tunnel.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class TunnelRestAPIs {

	@Autowired
	private TunnelService tunnelService;

	@PostMapping("/startEpcWriteAndLockIt")
	public void start(@RequestParam(value = "sku") String sku, 
			@RequestParam(value = "pack") @Min(10000) @Max(99999) Integer pack, 
			@RequestParam(value = "brand") @Min(1) @Max(32) Integer brand, 
			@RequestParam(value = "section") @Min(0) @Max(3) Integer section,
			@RequestParam(value = "password", required = true) String lockPsw,
			@RequestParam(value = "password", required = true) String unlockPsw) throws Exception, ResourceNotFoundException {
		try {

			tunnelService.startEpcWriteAndLockIt(sku, pack, brand, section, lockPsw, unlockPsw);

		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/stopEpcWriteAndLockIt")
	public void stop() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.stopEpcWriteAndLockIt();
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/startEpcLockAll")
	public void startEpcLockAll(@RequestParam(value = "password", required = true) String password) throws Exception, ResourceNotFoundException {
		try {

			tunnelService.startEpcLockAll(password);

		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/stopEpcLockAll")
	public void stopEpcLockAll() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.stopEpcLockAll();
		} catch (Exception e) {
			throw e;
		}
	}

}
