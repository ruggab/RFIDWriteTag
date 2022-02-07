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
import com.impinj.octane.TagWriteOp;
import com.impinj.octane.TagWriteOpResult;
import com.impinj.octane.TargetTag;
import com.impinj.octane.WordPointers;
import com.impinj.octane.WriteResultStatus;

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

public class LockAllEpc extends GenericJob implements TagReportListener, TagOpCompleteListener {
	Logger logger = Logger.getLogger(LockAllEpc.class);

	private InfoGenerator infoGenerator;
	private ImpinjReader reader;
	private TunnelService tunnelService;
	private InfoPackage infoPackage;

	public LockAllEpc(TunnelService tunnelService, InfoPackage infoPackage) {
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
			logger.debug("onTagReported contTagRep i: " + contTagRep);
			logger.debug("onTagReported: EPC: " + t.getEpc().toHexString());

			
			String currentEpc = t.getEpc().toHexString();
			String tid = t.getTid().toHexString();
			try {
				seqOp = seqOp + 1;
				TagOperation tagOp = this.tunnelService.getTagByTid(tid);
				if (tagOp == null || !tagOp.getPswWrited()) {
					accessPswWriteRequest(tagOp, tid, currentEpc, 1);
				} else if (!tagOp.getLocked() && tagOp.getPswWrited()) {
					lockEpcRequest(currentEpc);
				}
				//
			} catch (Exception e) {
				logger.error("onTagReported: Failed To program EPC: " + e.toString());
			}

		}
	}

	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		logger.debug("onTagOpComplete: ");
		contCmplOp = contCmplOp + 1;
		logger.debug("onTagReported contCmplOp: " + contCmplOp);
		for (TagOpResult t : results.getResults()) {
			if (t instanceof TagWriteOpResult) {
				TagWriteOpResult tr = (TagWriteOpResult) t;
				logger.debug("onTagOpComplete: Write OP seq id " + tr.getSequenceId());
				if (tr.getOpId() == WRITE_ACC_PSW_OP_ID) {
					if (tr.getResult() == WriteResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(tr.getTag().getTid().toHexString());
							tagOp.setPswWrited(true);
							tagOp.setIdOperation(WRITE_ACC_PSW_OP_ID);
							this.tunnelService.save(tagOp);
							logger.info("EPC Complete psw writed: " + tr.getTag().getTid().toHexString());
						} catch (Exception e) {
							logger.error(e);
						}
					}

				}
				logger.debug("onTagOpComplete: WRITE Access Psw EPC : " + tr.getTag().getEpc());
				logger.debug("onTagOpComplete: Status : " + tr.getResult());
				logger.debug("onTagOpComplete: Number of words written : " + tr.getNumWordsWritten());
				logger.debug("onTagOpComplete: result: " + tr.getResult().toString() + " words_written: " + tr.getNumWordsWritten());
				// outstanding--;
			} 
			if (t instanceof TagLockOpResult) {
				// Cast it to the correct type.
				// These are the results of locking the access password or user memory.
				TagLockOpResult lr = (TagLockOpResult) t;
				logger.debug("onTagOpComplete: lock OP seq id " + lr.getSequenceId());
				if (lr.getOpId() == LOCK_EPC_OP_ID) {
					if (lr.getResult() == LockResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(lr.getTag().getTid().toHexString());
							tagOp.setLocked(true);
							tagOp.setUnlocked(false);
							tagOp.setIdOperation(LOCK_EPC_OP_ID);
							this.tunnelService.save(tagOp);
							logger.info("EPC Complete locked: " + lr.getTag().getTid().toHexString());
						} catch (Exception e) {
							logger.error(e);
						}
					}
				}
				// Print out the results.
				logger.debug("onTagOpComplete: EPC Lock operation complete.");
				logger.debug("onTagOpComplete: EPC " + lr.getTag().getEpc());
				logger.debug("onTagOpComplete: Status " + lr.getResult());

			}

		}
	}

	private void lockEpcRequest(String currentEpc) throws Exception {
		logger.debug("lockEpcRequest:");
		TagOpSequence seq = new TagOpSequence();
		seq.setOps(new ArrayList<TagOp>());
		seq.setExecutionCount((short) 0); // forever
		seq.setState(SequenceState.Active);
		seq.setId(seqOp);
		seq = lockEpcOp(seq, currentEpc);
		reader.addOpSequence(seq);
	}

	private TagOpSequence lockEpcOp(TagOpSequence seq, String currentEpc) throws Exception {

		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);

		TagData td1 = TagData.fromHexString(infoGenerator.getInfo().createPasswordUnlock(currentEpc));

		TagLockOp lockOp = new TagLockOp();
		// lock the access password so it can't be changed
		// since we have a password set, we have to use it
		
		lockOp.setAccessPassword(td1);
		lockOp.setAccessPasswordLockType(TagLockState.Lock);
		lockOp.setEpcLockType(TagLockState.Lock);
		lockOp.Id = LOCK_EPC_OP_ID;
		//
		seq.getOps().add(lockOp);
		return seq;

	}

	private void accessPswWriteRequest(TagOperation tagOp, String tid, String currentEpc, int antenna) throws Exception {
		try {
			if (tagOp == null) {
				tagOp = new TagOperation();
				tagOp.setTid(tid);
				tagOp.setEpcOld(currentEpc);
				tagOp.setEpcNew("");
				tagOp.setLocked(false);
				tagOp.setEpcWrited(false);
				tagOp.setPswWrited(false);
				tagOp.setNumAntenna(1);
				tagOp.setSeqOperation(seqOp);
				this.tunnelService.save(tagOp);
			}
			TagOpSequence seq = new TagOpSequence();
			seq.setOps(new ArrayList<TagOp>());
			seq.setExecutionCount((short) 0); // forever
			seq.setState(SequenceState.Active);
			seq.setId(seqOp);
			seq = writeAccessPasswordOp(seq, currentEpc);
			reader.addOpSequence(seq);

		} catch (Exception e) {
			throw e;
		}
	}

	private TagOpSequence writeAccessPasswordOp(TagOpSequence seq, String currentEpc) throws Exception {
		logger.debug("writeAccessPasswordOp: ");
		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);
		// write a new access password
		TagData td1 = TagData.fromHexString(infoGenerator.getInfo().createPasswordlock(currentEpc));
		// TagData td2 = TagData.fromWord(new Integer(password));
		TagWriteOp writeOp = new TagWriteOp();
		writeOp.setMemoryBank(MemoryBank.Reserved);
		writeOp.setWordPointer(WordPointers.AccessPassword);
		writeOp.setData(td1);
		writeOp.Id = WRITE_ACC_PSW_OP_ID;
		// add to the list
		seq.getOps().add(writeOp);
		return seq;
	}

}