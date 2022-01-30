package net.smart.rfid.tunnel.db.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smart.rfid.tunnel.db.repository.TagOperationRepository;
import net.smart.rfid.tunnel.job.WriteEpc;
import net.smart.rfid.tunnel.model.InfoPackage;

@Service
public class TunnelService {

	Logger logger = Logger.getLogger(TunnelService.class);

	WriteEpc writeEpc = null;
	
	@Autowired
	TagOperationRepository tagOperationRepository;
	
	
	public void start(InfoPackage infoPackage) throws Exception {
		try {
			writeEpc = new WriteEpc(this, infoPackage);
			writeEpc.run();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	public void stop() throws Exception {

		try {
			writeEpc.stop();

		} catch (Exception ex) {
			logger.error("EXCEPTION DURING MANUAL STOP: " + ex.getMessage());
		}
	}

}