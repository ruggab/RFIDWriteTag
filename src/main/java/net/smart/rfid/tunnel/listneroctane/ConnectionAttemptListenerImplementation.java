package net.smart.rfid.tunnel.listneroctane;

import org.apache.log4j.Logger;

import com.impinj.octane.ConnectionAttemptEvent;
import com.impinj.octane.ConnectionAttemptListener;
import com.impinj.octane.ImpinjReader;

public class ConnectionAttemptListenerImplementation implements ConnectionAttemptListener {

	Logger logger = Logger.getLogger(ConnectionAttemptListenerImplementation.class);
	@Override
	public void onConnectionAttempt(ImpinjReader reader, ConnectionAttemptEvent e) {
		logger.info("Connection Attempt....................");
	}
}
