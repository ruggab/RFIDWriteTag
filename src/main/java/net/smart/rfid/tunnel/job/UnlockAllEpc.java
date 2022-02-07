package net.smart.rfid.tunnel.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.LockResultStatus;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.SearchMode;
import com.impinj.octane.SequenceState;
import com.impinj.octane.Settings;
import com.impinj.octane.Tag;
import com.impinj.octane.TagData;
import com.impinj.octane.TagLockOp;
import com.impinj.octane.TagLockOpResult;
import com.impinj.octane.TagLockState;
import com.impinj.octane.TagOp;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagOpReport;
import com.impinj.octane.TagOpResult;
import com.impinj.octane.TagOpSequence;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import com.impinj.octane.TargetTag;

import net.smart.rfid.tunnel.db.entity.TagOperation;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.model.InfoPackage;
import net.smart.rfid.tunnel.util.InfoGenerator;
import net.smart.rfid.tunnel.util.InfoGeneratorFactory;
import net.smart.rfid.tunnel.util.PropertiesUtil;

/**
 * 
 * 
 */

public class UnlockAllEpc implements TagReportListener, TagOpCompleteListener {
	Logger logger = Logger.getLogger(UnlockAllEpc.class);

	static short LOCK_ACC_PSW_OP_ID = 0;
	static short LOCK_EPC_OP_ID = 1;
	static short UNLOCK_EPC_OP_ID = 10;
	static short WRITE_EPC_OP_ID = 20;
	static short WRITE_PC_OP_ID = 30;
	static short WRITE_ACC_PSW_OP_ID = 40;
	static Integer seqOp = 1;
	static int contTagRep = 0;
	static int contCmplOp = 0;
	private InfoGenerator infoGenerator;

	private ImpinjReader reader;

	private TunnelService tunnelService;
	private InfoPackage infoPackage;

	public UnlockAllEpc(TunnelService tunnelService, InfoPackage infoPackage) {
		this.infoPackage = infoPackage;
		this.tunnelService = tunnelService;
		this.infoGenerator = InfoGeneratorFactory.createInfoGenerator(infoPackage);
	}

	public void run() {

		try {
			String hostname = PropertiesUtil.getHostname();

			if (hostname == null) {
				throw new Exception("Must specify the '" + PropertiesUtil.getHostname() + "' property");
			}

			reader = new ImpinjReader();

			// Connect
			logger.debug("Connecting to " + hostname);
			reader.connect(hostname);

			// Get the default settings
			Settings settings = reader.queryDefaultSettings();

			// just use a single antenna here
			settings.getAntennas().disableAll();
			settings.getAntennas().getAntenna((short) 1).setEnabled(true);
			settings.getAntennas().getAntenna((short) 1).setIsMaxRxSensitivity(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 1).setIsMaxTxPower(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 1).setTxPowerinDbm(Double.valueOf(10));
			settings.getAntennas().getAntenna((short) 1).setRxSensitivityinDbm(Double.valueOf(-70));

			// set session one so we see the tag only once every few seconds
			settings.getReport().setIncludeAntennaPortNumber(true);
			settings.setRfMode(1000);
			settings.setSearchMode(SearchMode.DualTarget);
			settings.setSession(0);
			// turn these on so we have them always
			settings.getReport().setIncludePcBits(true);

			// Set periodic mode so we reset the tag and it shows up with its
			// new EPC
			// settings.getAutoStart().setMode(AutoStartMode.Periodic);
			// settings.getAutoStart().setPeriodInMs(2000);
			// settings.getAutoStop().setMode(AutoStopMode.Duration);
			// settings.getAutoStop().setDurationInMs(1000);

			ReportConfig r = settings.getReport();
			// settings.getReport().setIncludeAntennaPortNumber(true);

			// tell the reader to include the antenna port number in the report
			r.setIncludeAntennaPortNumber(true);
			r.setIncludeFirstSeenTime(true);
			r.setIncludeChannel(true);
			r.setIncludeCrc(true);
			r.setIncludeDopplerFrequency(true);
			r.setIncludeFastId(true);
			r.setIncludeLastSeenTime(true);
			r.setIncludeLastSeenTime(true);
			r.setIncludePeakRssi(true);
			r.setIncludePhaseAngle(true);
			r.setIncludeSeenCount(true);
			settings.setReport(r);

			// Apply the new settings
			reader.applySettings(settings);

			// set up listeners to hear stuff back from SDK
			reader.setTagReportListener(this);
			reader.setTagOpCompleteListener(this);

			// Start the reader
			reader.start();

			logger.debug("STARTED");
		} catch (OctaneSdkException ex) {
			logger.error(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}

	public void stop() throws OctaneSdkException {
		logger.debug("Stopping  ");
		reader.stop();

		logger.debug("Disconnecting from ");
		reader.disconnect();

	}

	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		contTagRep = contTagRep + 1;
		logger.debug("onTagReported contTagRep: " + contTagRep);
		for (Tag t : tags) {
			String currentEpc = t.getEpc().toHexString();
			String tid = t.getTid().toHexString();
			try {
				seqOp = seqOp + 1;
				TagOperation tagOp = this.tunnelService.getTagByTid(tid);
				if (!tagOp.getUnlocked().booleanValue()) {
					logger.debug("onTagReported EPC unlockEpcRequest: " + t.getEpc().toHexString());
					unlockEpcRequest(currentEpc);
				}
				//
			} catch (Exception e) {
				logger.error("onTagReported: Failed To program EPC: " + e.toString());
			}

		}
	}

	private void unlockEpcRequest(String currentEpc) throws Exception {
		logger.debug("unlockEpcRequest:");
		TagOpSequence seq = new TagOpSequence();
		seq.setOps(new ArrayList<TagOp>());
		seq.setExecutionCount((short) 0); // forever
		seq.setState(SequenceState.Active);
		seq.setId(seqOp);
		seq = unlockEpcOp(seq, currentEpc);
		reader.addOpSequence(seq);
	}

	private TagOpSequence unlockEpcOp(TagOpSequence seq, String currentEpc) throws Exception {

		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);
		// write a new access password

		TagData td1 = TagData.fromHexString(infoGenerator.getInfo().createPasswordUnlock(currentEpc));

		TagLockOp lockOp = new TagLockOp();
		// lock the access password so it can't be changed
		// since we have a password set, we have to use it
		lockOp.Id = UNLOCK_EPC_OP_ID;
		lockOp.setAccessPassword(td1);
		lockOp.setEpcLockType(TagLockState.Unlock);
		//
		seq.getOps().add(lockOp);
		return seq;

	}

	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		logger.debug("onTagOpComplete: ");
		contCmplOp = contCmplOp + 1;
		logger.debug("onTagReported contCmplOp: " + contCmplOp);
		for (TagOpResult t : results.getResults()) {
			if (t instanceof TagLockOpResult) {
				// Cast it to the correct type.
				// These are the results of locking the access password or user memory.
				TagLockOpResult lr = (TagLockOpResult) t;
				if (lr.getOpId() == UNLOCK_EPC_OP_ID) {
					if (lr.getResult() == LockResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(lr.getTag().getTid().toHexString());
							tagOp.setLocked(false);
							tagOp.setUnlocked(true);
							tagOp.setIdOperation(LOCK_EPC_OP_ID);
							this.tunnelService.save(tagOp);
							logger.info("EPC Complete unlocked: " + lr.getTag().getTid().toHexString());
						} catch (Exception e) {
							logger.error(e);
						}
					}
				}
				// Print out the results.
				logger.debug("onTagOpComplete: EPC unlocked operation complete.");
				logger.debug("onTagOpComplete: EPC " + lr.getTag().getEpc());
				logger.debug("onTagOpComplete: Status " + lr.getResult());

			}

		}
	}

}