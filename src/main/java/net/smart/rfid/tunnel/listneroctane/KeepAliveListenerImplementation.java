package net.smart.rfid.tunnel.listneroctane;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.KeepaliveEvent;
import com.impinj.octane.KeepaliveListener;

public class KeepAliveListenerImplementation implements KeepaliveListener {

	private static final Logger LOGGER = Logger.getLogger(KeepAliveListenerImplementation.class);
	static net.smart.rfid.tunnel.util.DateFunction myDate;

	public KeepAliveListenerImplementation() {

	}

	public void onKeepalive(ImpinjReader reader, KeepaliveEvent e) {
	}

}
