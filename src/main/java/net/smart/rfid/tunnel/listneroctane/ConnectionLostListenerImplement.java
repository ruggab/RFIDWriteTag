package net.smart.rfid.tunnel.listneroctane;

import org.apache.log4j.Logger;

import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.ImpinjReader;

import net.smart.rfid.tunnel.util.DateFunction;

public class ConnectionLostListenerImplement implements ConnectionLostListener {
	static DateFunction myDate;
	private static final Logger logger = Logger.getLogger(ConnectionLostListenerImplement.class);

	public ConnectionLostListenerImplement() {
	
	}

	@Override
	public void onConnectionLost(ImpinjReader reader) {
		
		logger.info("ATTENTION  ConnectionLostListenerImplement");
		
	}
}
