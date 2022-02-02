package net.smart.rfid.tunnel.listneroctane;


import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStopEvent;
import com.impinj.octane.ReaderStopListener;

public class ReaderStopListenerImplementation implements ReaderStopListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	
	
	public ReaderStopListenerImplementation() {
	}
	
	
    @Override
    public void onReaderStop(ImpinjReader reader, ReaderStopEvent e) {
    	logger.debug("Listener - Reader_Stopped");
    }
}
