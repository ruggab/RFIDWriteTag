package net.smart.rfid.tunnel.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
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
	Logger logger = Logger.getLogger(TunnelRestAPIs.class);

	@Autowired
	private TunnelService tunnelService;

	@PostMapping("/EpcUnlockWriteLockStart")
	public void epcUnlockWriteLockStart(@RequestParam(value = "dbmAntenna1") @Min(10) @Max(30) Integer dbmAntenna1,
			@RequestParam(value = "dbmAntenna2") @Min(10) @Max(30) Integer dbmAntenna2,
			@RequestParam(value = "dbmAntenna3") @Min(10) @Max(30) Integer dbmAntenna3,
			@RequestParam(value = "sku") @NotBlank @Size(min = 14) @Size(max = 14) String sku, 
			@RequestParam(value = "pack") @Min(10000) @Max(99999) Integer pack, 
			@RequestParam(value = "brand") @Min(1) @Max(32) Integer brand, 
			@RequestParam(value = "section") @Min(0) @Max(2) Integer section,
			@RequestParam(value = "lockPsw") String lockPsw) throws Exception, ResourceNotFoundException {
		try {
			tunnelService.epcUnlockWriteLockStart(dbmAntenna1, dbmAntenna2, dbmAntenna3, sku, pack, brand, section, lockPsw);
			logger.debug("Start Tunnel");
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/EpcUnlockWriteLockStop")
	public void epcUnlockWriteLockStop() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.epcUnlockWriteLockStop();
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/NewAccessPswWriteEpcLockStart")
	public void newAccessPswWriteEpcLockStart(@RequestParam(value = "password", required = true) String password) throws Exception, ResourceNotFoundException {
		try {

			tunnelService.newAccessPswWriteEpcLockStart(password);

		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/NewAccessPswWriteEpcLockStop")
	public void newAccessPswWriteEpcLockStop() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.newAccessPswWriteEpcLockStop();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@PostMapping("/EpcUnlockStart")
	public void epcUnlockStart(@RequestParam(value = "password", required = true) String password) throws Exception, ResourceNotFoundException {
		try {
			tunnelService.epcUnlockStart(password);
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/EpcUnlockStop")
	public void epcUnlockStop() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.epcUnlockStop();
		} catch (Exception e) {
			throw e;
		}
	}

}
