package net.smart.rfid.tunnel.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.LockResultStatus;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.PcBits;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
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
import net.smart.rfid.tunnel.listneroctane.TagReportListenerImplementation;
import net.smart.rfid.tunnel.model.InfoPackage;
import net.smart.rfid.tunnel.util.PropertiesUtil;

/**
 * 
 * 
 */

public class WriteEpc implements TagReportListener, TagOpCompleteListener {
	Logger logger = Logger.getLogger(WriteEpc.class);

	static short LOCK_ACC_PSW_OP_ID = 0;
	static short LOCK_EPC_OP_ID = 1;
	static short UNLOCK_EPC_OP_ID = 10;
	static short WRITE_EPC_OP_ID = 20;
	static short WRITE_PC_OP_ID = 30;
	static short WRITE_ACC_PSW_OP_ID = 40;
	static Integer opSecID = 1;
	static int contTagRep = 0;
	static int contCmplOp = 0;
	// static int outstanding = 0;
	static int index = 0;
	private ImpinjReader reader;

	private TunnelService tunnelService;
	private InfoPackage infoPackage;

	public WriteEpc(TunnelService tunnelService, InfoPackage infoPackage) {
		this.infoPackage = infoPackage;
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
			//
			settings.getAntennas().getAntenna((short) 1).setEnabled(true);
			settings.getAntennas().getAntenna((short) 1).setIsMaxRxSensitivity(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 1).setIsMaxTxPower(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 1).setTxPowerinDbm(Double.valueOf(10));
			settings.getAntennas().getAntenna((short) 1).setRxSensitivityinDbm(Double.valueOf(-70));
			//settings.getAntennas().getAntenna((short) 1).setIsMaxTxPower(false);

			// just use a single antenna here
			
			settings.getAntennas().getAntenna((short) 2).setEnabled(true);
			settings.getAntennas().getAntenna((short) 2).setIsMaxRxSensitivity(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 2).setIsMaxTxPower(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 2).setTxPowerinDbm(Double.valueOf(10));
			settings.getAntennas().getAntenna((short) 2).setRxSensitivityinDbm(Double.valueOf(-70));
			//settings.getAntennas().getAntenna((short) 2).setIsMaxTxPower(false);

			// just use a single antenna here
			
			settings.getAntennas().getAntenna((short) 3).setEnabled(true);
			settings.getAntennas().getAntenna((short) 3).setIsMaxRxSensitivity(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 3).setIsMaxTxPower(Boolean.valueOf(false));
			settings.getAntennas().getAntenna((short) 3).setTxPowerinDbm(Double.valueOf(10));
			settings.getAntennas().getAntenna((short) 3).setRxSensitivityinDbm(Double.valueOf(-70));
			//settings.getAntennas().getAntenna((short) 3).setIsMaxTxPower(false);
			

			// set session one so we see the tag only once every few seconds
			
			//
			settings.setRfMode(1000);
			settings.setSearchMode(SearchMode.DualTarget);
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

		logger.info("onTagReported contTagRep: " + contTagRep);
		for (Tag t : tags) {
			index++;
			// Metto quattro 0 avanti alla stringa
			String appo = "";// String.format("%04d", index);
			logger.info("onTagReported: EPC: " + t.getEpc().toHexString());
			/// ANTENNA 1 PER UNLOCK
			if (t.getAntennaPortNumber() == 1 && t.isPcBitsPresent()) {
				short pc = t.getPcBits();
				String currentEpc = t.getEpc().toHexString();
				String tid = t.getTid().toHexString();
				try {
					// Recupero il tag
					opSecID = opSecID + 1;
					TagOperation tagOp = this.tunnelService.getTagByTid(tid);
					unlockRequest(tagOp, tid, currentEpc, pc);

				} catch (Exception e) {
					logger.error("onTagReported: Failed To program EPC: " + e.toString());
				}
			}
			/// ANTENNA 2 PER SCRITTURA o UNLOCK
			if (t.getAntennaPortNumber() == 2 && t.isPcBitsPresent()) {
				short pc = t.getPcBits();
				String currentEpc = t.getEpc().toHexString();
				String tid = t.getTid().toHexString();
				String newEpc = infoPackage.getPack() + currentEpc.substring(5, currentEpc.length());
				try {
					opSecID = opSecID + 1;
					TagOperation tagOp = this.tunnelService.getTagByTid(tid);
					if (tagOp == null || !tagOp.getUnlocked()) {
						unlockRequest(tagOp, tid, currentEpc, pc);
					} else if (StringUtils.isEmpty(tagOp.getEpcNew()) || !tagOp.getWrited().booleanValue()) {
						writeRequest(tagOp, currentEpc, newEpc, t.getAntennaPortNumber(), pc);
					}

					//
				} catch (Exception e) {
					logger.error("onTagReported: Failed To program EPC: " + e.toString());
				}
			}
			/// ANTENNA 3 PER LOCK o SCRITTURA O UNLOCK
			if (t.getAntennaPortNumber() == 3 && t.isPcBitsPresent()) {
				short pc = t.getPcBits();
				String currentEpc = t.getEpc().toHexString();
				String tid = t.getTid().toHexString();
				String newEpc = infoPackage.getPack() + currentEpc.substring(5, currentEpc.length());
				try {
					opSecID = opSecID + 1;
					TagOperation tagOp = this.tunnelService.getTagByTid(tid);
					if (tagOp == null || !tagOp.getUnlocked()) {
						unlockRequest(tagOp, tid, currentEpc, pc);
					} else if (StringUtils.isEmpty(tagOp.getEpcNew()) || !tagOp.getWrited().booleanValue()) {
						writeRequest(tagOp, currentEpc, newEpc, t.getAntennaPortNumber(), pc);
					} else if (!StringUtils.isEmpty(tagOp.getEpcNew()) && tagOp.getWrited().booleanValue()) {
						opSecID = opSecID + 1;
						lockRequest(tagOp, currentEpc, t.getAntennaPortNumber());
					}
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
				//
				if (tr.getOpId() == WRITE_EPC_OP_ID) {
					// Se il tag è stato scritto con successo allora faccio update nel db in modo da non lavorare piu
					// questo epc
					logger.info("onTagOpComplete:  Write EPC Complete: ");
					if (tr.getResult() == WriteResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(tr.getTag().getTid().toHexString());
							tagOp.setWrited(true);
							this.tunnelService.save(tagOp);
						} catch (Exception e) {
							logger.error(e);
						}
					}
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

			}
			if (t instanceof TagLockOpResult) {
				// Cast it to the correct type.
				// These are the results of locking the access password or user memory.
				TagLockOpResult lr = (TagLockOpResult) t;

				if (lr.getOpId() == UNLOCK_EPC_OP_ID) {
					// Se il tag è stato scritto con successo allora faccio update nel db in modo da non lavorare piu
					// questo epc
					logger.info("onTagOpComplete:  unlock EPC Complete: ");
					if (lr.getResult() == LockResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(lr.getTag().getTid().toHexString());
							tagOp.setUnlocked(true);
							this.tunnelService.save(tagOp);
						} catch (Exception e) {
							logger.error(e);
						}

					}
				}
				if (lr.getOpId() == LOCK_EPC_OP_ID) {
					// Se il tag è stato scritto con successo allora faccio update nel db in modo da non lavorare piu
					// questo epc
					logger.info("onTagOpComplete:  LOCK EPC Complete: ");
					if (lr.getResult() == LockResultStatus.Success) {
						try {
							TagOperation tagOp = this.tunnelService.getTagByTid(lr.getTag().getTid().toHexString());
							tagOp.setLocked(true);
							this.tunnelService.save(tagOp);
						} catch (Exception e) {
							logger.error(e);
						}

					}
				}
				logger.info("onTagOpComplete: lock OP seq id " + lr.getSequenceId());
				// Print out the results.
				logger.info("onTagOpComplete: Lock operation complete.");
				logger.info("onTagOpComplete: EPC " + lr.getTag().getEpc());
				logger.info("onTagOpComplete: Status " + lr.getResult());

			}

		}
	}

	private TagOpSequence writeAccessPassword(TagOpSequence seq, String currentEpc, String password) throws Exception {

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

	private TagOpSequence lockTag(TagOpSequence seq, String currentEpc, String password) throws Exception {
		logger.info("lockTag:");
		logger.info("lockTag: EPC " + currentEpc);
		//
		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);
		TagLockOp lockOp = new TagLockOp();
		// lock the access password so it can't be changed
		// since we have a password set, we have to use it
		lockOp.setAccessPassword(TagData.fromHexString(password));
		// lockOp.setAccessPasswordLockType(TagLockState.Lock);
		lockOp.Id = LOCK_EPC_OP_ID;
		// uncomment to lock user memory so it can't be changed
		lockOp.setEpcLockType(TagLockState.Lock);

		// add to the list
		seq.getOps().add(lockOp);
		//
		return seq;
	}

	private TagOpSequence unlockTag(TagOpSequence seq, String currentEpc, String password) throws Exception {
		//
		logger.info("unlockTag:");
		logger.info("unlockTag: EPC " + currentEpc);
		// Effettuo questa operazione solo alla currentTag
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);
		//
		TagLockOp lockOp = new TagLockOp();
		// lock the access password so it can't be changed
		// since we have a password set, we have to use it
		lockOp.setAccessPassword(TagData.fromHexString(password));
		// lockOp.setAccessPasswordLockType(TagLockState.Unlock);
		// uncomment to lock user memory so it can't be changed
		lockOp.Id = UNLOCK_EPC_OP_ID;
		lockOp.setEpcLockType(TagLockState.Unlock);
		// add to the list
		seq.getOps().add(lockOp);
		//
		return seq;
	}

	private TagOpSequence programEpc(TagOpSequence seq, String currentEpc, short currentPC, String newEpc) throws Exception {
		if ((currentEpc.length() % 4 != 0) || (newEpc.length() % 4 != 0)) {
			throw new Exception("EPCs must be a multiple of 16- bits: " + currentEpc + "  " + newEpc);
		}

		logger.info("Programming Tag ");
		logger.info("EPC " + currentEpc + " to " + newEpc);

		//
		seq.setTargetTag(new TargetTag());
		seq.getTargetTag().setBitPointer(BitPointers.Epc);
		seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
		seq.getTargetTag().setData(currentEpc);

		//
		TagWriteOp epcWrite = new TagWriteOp();
		epcWrite.Id = WRITE_EPC_OP_ID;
		epcWrite.setMemoryBank(MemoryBank.Epc);
		epcWrite.setWordPointer(WordPointers.Epc);
		epcWrite.setData(TagData.fromHexString(newEpc));

		// add to the list
		seq.getOps().add(epcWrite);

		// have to program the PC bits if these are not the same
		if (currentEpc.length() != newEpc.length()) {
			// keep other PC bits the same.
			String currentPCString = PcBits.toHexString(currentPC);

			short newPC = PcBits.AdjustPcBits(currentPC, (short) (newEpc.length() / 4));
			String newPCString = PcBits.toHexString(newPC);

			logger.info("PC bits to establish new length: " + newPCString + " " + currentPCString);

			TagWriteOp pcWrite = new TagWriteOp();
			pcWrite.Id = WRITE_PC_OP_ID;
			pcWrite.setMemoryBank(MemoryBank.Epc);
			pcWrite.setWordPointer(WordPointers.PcBits);

			pcWrite.setData(TagData.fromHexString(newPCString));
			seq.getOps().add(pcWrite);
		}

		// outstanding++;
		return seq;
	}

	private void unlockRequest(TagOperation tagOp, String tid, String currentEpc, int antenna) throws Exception {
		try {
			if (tagOp == null) {
				tagOp = new TagOperation();
				tagOp.setTid(tid);
				tagOp.setEpcOld(currentEpc);
				tagOp.setEpcNew("");
				tagOp.setUnlocked(false);
				tagOp.setLocked(false);
				tagOp.setWrited(false);
				tagOp.setNumAntenna(antenna);
				tagOp.setIdOperation(UNLOCK_EPC_OP_ID);
				tagOp.setSeqOperation(opSecID);
				this.tunnelService.save(tagOp);
			} else {
				// if not unlocked request unlocked op for it
				if (!tagOp.getUnlocked().booleanValue()) {
					TagOpSequence seq1 = new TagOpSequence();
					seq1.setOps(new ArrayList<TagOp>());
					seq1.setExecutionCount((short) 1); // forever
					seq1.setState(SequenceState.Active);
					seq1.setId(opSecID);
					seq1 = unlockTag(seq1, currentEpc, infoPackage.getPswUnlock());
					reader.addOpSequence(seq1);

				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void writeRequest(TagOperation tagOp, String currentEpc, String newEpc, int antenna, short pc) throws Exception {
		try {
			tagOp.setEpcNew(newEpc);
			tagOp.setNumAntenna(antenna);
			tagOp.setIdOperation(WRITE_EPC_OP_ID);
			tagOp.setSeqOperation(opSecID);
			this.tunnelService.save(tagOp);
			//
			TagOpSequence seq = new TagOpSequence();
			seq.setOps(new ArrayList<TagOp>());
			seq.setExecutionCount((short) 1); // forever
			seq.setState(SequenceState.Active);
			seq.setId(opSecID);
			seq = programEpc(seq, currentEpc, pc, newEpc);
			reader.addOpSequence(seq);

		} catch (Exception e) {
			throw e;
		}
	}

	private void lockRequest(TagOperation tagOp, String currentEpc, int antenna) throws Exception {
		//
		tagOp.setNumAntenna(antenna);
		tagOp.setIdOperation(LOCK_EPC_OP_ID);
		tagOp.setSeqOperation(opSecID);
		this.tunnelService.save(tagOp);
		//
		TagOpSequence seq2 = new TagOpSequence();
		seq2.setOps(new ArrayList<TagOp>());
		seq2.setExecutionCount((short) 1); // forever
		seq2.setState(SequenceState.Active);
		seq2.setId(opSecID++);
		seq2 = lockTag(seq2, currentEpc, infoPackage.getPswLock());
		reader.addOpSequence(seq2);
		//
		
	}
}