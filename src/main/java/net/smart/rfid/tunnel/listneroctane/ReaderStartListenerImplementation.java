package net.smart.rfid.tunnel.listneroctane;


import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStartEvent;
import com.impinj.octane.ReaderStartListener;

public class ReaderStartListenerImplementation implements ReaderStartListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	
	
	public ReaderStartListenerImplementation() {
	}
    @Override
    public void onReaderStart(ImpinjReader reader, ReaderStartEvent e) {
    	logger.info("Listener - Reader_Started");
    }
}
