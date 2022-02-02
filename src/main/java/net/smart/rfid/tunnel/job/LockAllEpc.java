package net.smart.rfid.tunnel.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.PcBits;
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

import net.smart.rfid.tunnel.db.entity.TagOperation;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.model.InfoPackage;
import net.smart.rfid.tunnel.util.PropertiesUtil;

/**
 * 
 * 
 */

public class LockAllEpc implements TagReportListener, TagOpCompleteListener {
	Logger logger = Logger.getLogger(LockAllEpc.class);

	static short WRITE_EPC_OP_ID = 111;
	static short WRITE_PC_OP_ID = 222;
	static short WRITE_ACC_PSW_OP_ID = 333;
	static short LOCK_ACC_PSW_OP_ID = 444;
	static short UNLOCK_USER_OP_ID = 555;
	static short LOCK_USER_OP_ID = 666;
	static int opSpecID = 1;
	static int contTagRep = 0;
	static int contCmplOp = 0;
	// static int outstanding = 0;
	static int index = 0;
	private ImpinjReader reader;

	private TunnelService tunnelService;
	private String password;

	public LockAllEpc(TunnelService tunnelService, String password) {
		this.password = password;
		this.tunnelService = tunnelService;
	}

	public void run() {

		try {
			String hostname = PropertiesUtil.getHostname();

			if (hostname == null) {
				throw new Exception("Must specify the '" + PropertiesUtil.getHostname() + "' property");
			}

			reader = new ImpinjReader();

			// Connect
			logger.info("Connecting to " + hostname);
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
			settings.setSearchMode(SearchMode.SingleTarget);
			settings.setSession(0);
			// turn these on so we have them always
			settings.getReport().setIncludePcBits(true);

			// Set periodic mode so we reset the tag and it shows up with its
			// new EPC
			settings.getAutoStart().setMode(AutoStartMode.Periodic);
			settings.getAutoStart().setPeriodInMs(2000);
			settings.getAutoStop().setMode(AutoStopMode.Duration);
			settings.getAutoStop().setDurationInMs(1000);
			
			ReportConfig r = settings.getReport();
			//settings.getReport().setIncludeAntennaPortNumber(true);
		
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

			logger.info("STARTED");
		} catch (OctaneSdkException ex) {
			logger.error(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace(System.out);
		}
	}

	public void stop() throws OctaneSdkException {
		logger.info("Stopping  ");
		reader.stop();

		logger.info("Disconnecting from ");
		reader.disconnect();

	}

	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		contTagRep = contTagRep + 1;
		logger.info("onTagReported contTagRep: " + contTagRep);
		for (Tag t : tags) {
			logger.info("onTagReported contTagRep i: " + contTagRep);
			index++;
			String appo = "";// String.format("%04d", index);
			logger.info("onTagReported: EPC: " + t.getEpc().toHexString());

			if (t.isPcBitsPresent()) {
				short pc = t.getPcBits();
				String currentEpc = t.getEpc().toHexString();
				try {

					//
					TagOpSequence seq1 = new TagOpSequence();
					seq1.setOps(new ArrayList<TagOp>());
					seq1.setExecutionCount((short) 0); // forever
					seq1.setState(SequenceState.Active);
					seq1.setId(opSpecID++);
					seq1 = writeAccessPasswordAndLockEpc(seq1, currentEpc, this.password);
					reader.addOpSequence(seq1);

					//
				} catch (Exception e) {
					logger.error("onTagReported: Failed To program EPC: " + e.toString());
				}
			}

		}
	}

	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		logger.info("onTagOpComplete: ");
		contCmplOp = contCmplOp + 1;
		logger.info("onTagReported contCmplOp: " + contCmplOp);
		for (TagOpResult t : results.getResults()) {
			if (t instanceof TagWriteOpResult) {
				TagWriteOpResult tr = (TagWriteOpResult) t;
				logger.info("onTagOpComplete: Write OP seq id " + tr.getSequenceId());
				if (tr.getOpId() == WRITE_EPC_OP_ID) {
					logger.info("onTagOpComplete:  Write EPC Complete: ");
				}
				if (tr.getOpId() == WRITE_PC_OP_ID) {
					logger.info("onTagOpComplete:  Write PC Complete: ");
				}
				if (tr.getOpId() == WRITE_ACC_PSW_OP_ID) {
					logger.info("onTagOpComplete:  Write PASSWORD Complete: ");
				}
				logger.info("onTagOpComplete: EPC : " + tr.getTag().getEpc());
				logger.info("onTagOpComplete: Status : " + tr.getResult());
				logger.info("onTagOpComplete: Number of words written : " + tr.getNumWordsWritten());
				logger.info("onTagOpComplete: result: " + tr.getResult().toString() + " words_written: " + tr.getNumWordsWritten());
				// outstanding--;
			} else if (t instanceof TagLockOpResult) {
				// Cast it to the correct type.
				// These are the results of locking the access password or user memory.
				TagLockOpResult lr = (TagLockOpResult) t;
				logger.info("onTagOpComplete: lock OP seq id " + lr.getSequenceId());
				if (lr.getOpId() == LOCK_ACC_PSW_OP_ID) {
					logger.info("onTagOpComplete:  LOCK ACC PSW ");
				}
				if (lr.getOpId() == UNLOCK_USER_OP_ID) {
					logger.info("onTagOpComplete:  UNLOCK USER ");
				}
				if (lr.getOpId() == LOCK_USER_OP_ID) {
					logger.info("onTagOpComplete:  LOCK USER ");
				}
				// Print out the results.
				logger.info("onTagOpComplete: Lock operation complete.");
				logger.info("onTagOpComplete: EPC " + lr.getTag().getEpc());
				logger.info("onTagOpComplete: Status " + lr.getResult());

			}

		}
	}

	private TagOpSequence writeAccessPasswordAndLockEpc(TagOpSequence seq, String currentEpc, String password) throws Exception {

		logger.info("writeAccessPassword: Write Access Password  and lock it");

		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);
		// write a new access password
		TagWriteOp writeOp = new TagWriteOp();
		writeOp.setMemoryBank(MemoryBank.Reserved);
		// access password starts at an offset into reserved memory
		writeOp.setWordPointer(WordPointers.AccessPassword);
		writeOp.setData(TagData.fromHexString(password));
		writeOp.Id = WRITE_ACC_PSW_OP_ID;
		// no access password at this point

		TagLockOp lockOp = new TagLockOp();
		// lock the access password so it can't be changed
		// since we have a password set, we have to use it
		lockOp.Id = LOCK_ACC_PSW_OP_ID;
		lockOp.setAccessPassword(TagData.fromHexString(password));
		lockOp.setAccessPasswordLockType(TagLockState.Lock);
		lockOp.setEpcLockType(TagLockState.Lock);
		// add to the list
		seq.getOps().add(writeOp);
		seq.getOps().add(lockOp);

		return seq;

	}

}