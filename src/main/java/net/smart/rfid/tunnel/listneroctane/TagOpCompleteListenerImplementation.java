package net.smart.rfid.tunnel.listneroctane;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.TagLockOpResult;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagOpReport;
import com.impinj.octane.TagOpResult;
import com.impinj.octane.TagWriteOpResult;

public class TagOpCompleteListenerImplementation implements TagOpCompleteListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);


	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		// Loop through all the completed tag operations
         
		for (TagOpResult t : results.getResults()) {
		
			if (t instanceof TagWriteOpResult) {
                // These are the results of settings the access password.
                // Cast it to the correct type.
				TagWriteOpResult tr = (TagWriteOpResult) t;
                // Print out the results.
				logger.info("Set access password complete.");
				logger.info("EPC : " + tr.getTag().getEpc());
				logger.info("Status : " + tr.getResult());
				logger.info("Number of words written : " + tr.getNumWordsWritten());
            }
            else if (t instanceof  TagLockOpResult) {
                // Cast it to the correct type.
                // These are the results of locking the access password or user memory.
                TagLockOpResult lr = (TagLockOpResult) t;
                // Print out the results.
                logger.info("Lock operation complete.");
                logger.info("EPC : {0}" + lr.getTag().getEpc());
                logger.info("Status : {0}" + lr.getResult());
            }
        }
	}

}
