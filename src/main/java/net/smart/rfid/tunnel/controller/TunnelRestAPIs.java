package net.smart.rfid.tunnel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.exception.ResourceNotFoundException;
import net.smart.rfid.tunnel.model.InfoPackage;

@RestController 
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class TunnelRestAPIs {

	@Autowired
	private TunnelService tunnelService;

	@PostMapping("/startTunnel")
	public void start(@RequestBody InfoPackage infoPackage) throws Exception, ResourceNotFoundException {
		try {

			tunnelService.start(infoPackage);

		} catch (Exception e) {
			throw e;
		}

	} 

	@PostMapping("/stopTunnel")
	public void stop() throws Exception, ResourceNotFoundException {
		try {
			tunnelService.stop();
		} catch (Exception e) {
			throw e;
		}
	}

}
