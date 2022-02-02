package net.smart.rfid.tunnel.listneroctane;

import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);

	public TagReportListenerImplementation() {
	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {

		List<Tag> tags = report.getTags();

		for (Tag t : tags) {
			logger.debug("EPC: " + t.getEpc().toHexString());
		}
	}

}
