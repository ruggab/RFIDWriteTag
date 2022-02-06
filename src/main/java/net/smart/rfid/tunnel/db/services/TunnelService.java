package net.smart.rfid.tunnel.db.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smart.rfid.tunnel.db.entity.TagOperation;
import net.smart.rfid.tunnel.db.repository.TagOperationRepository;
import net.smart.rfid.tunnel.job.LockAllEpc;
import net.smart.rfid.tunnel.job.UnlockAllEpc;
import net.smart.rfid.tunnel.job.WriteEpc;
import net.smart.rfid.tunnel.model.ConfTunnel;
import net.smart.rfid.tunnel.model.InfoPackage;

@Service
public class TunnelService {

	Logger logger = Logger.getLogger(TunnelService.class);

	WriteEpc writeEpc = null;
	LockAllEpc lockAllEpc = null;
	UnlockAllEpc unlockAllEpc = null;

	@Autowired
	TagOperationRepository tagOperationRepository;

	public void epcUnlockWriteLockStart(Integer dbm1,Integer dbm2, Integer dbm3, String sku, Integer pack, Integer brand, Integer section, String lockPsw) throws Exception {
		try {
			ConfTunnel confTunnel = new ConfTunnel(dbm1,dbm2,dbm3,10);
			InfoPackage infoPackage = new InfoPackage(sku, pack, brand, section, lockPsw);
			writeEpc = new WriteEpc(this, infoPackage, confTunnel);
			writeEpc.run();
		} catch (Exception e) {
			logger.error(e.getMessage());

		}

	}

	public void epcUnlockWriteLockStop() throws Exception {
		try {
			writeEpc.stop();
		} catch (Exception ex) {
			logger.error("EXCEPTION DURING MANUAL STOP: " + ex.getMessage());
		}
	}
	
	
	public void newAccessPswWriteEpcLockStart(String password) throws Exception {
		try {
			lockAllEpc = new LockAllEpc(this, password);
			lockAllEpc.run();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void newAccessPswWriteEpcLockStop() throws Exception {
		try {
			lockAllEpc.stop();
		} catch (Exception ex) {
			logger.error("EXCEPTION DURING MANUAL STOP: " + ex.getMessage());
		}
	}

	
	
	public void epcUnlockStart(String password) throws Exception {
		try {
			unlockAllEpc = new UnlockAllEpc(this, password);
			unlockAllEpc.run();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void epcUnlockStop() throws Exception {
		try {
			unlockAllEpc.stop();
		} catch (Exception ex) {
			logger.error("EXCEPTION DURING MANUAL STOP: " + ex.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	public TagOperation isEpcWritedAndNotLocked(String epcNew) throws Exception {
		//Ritorno true se il tag è scritto ma non lockato
		TagOperation tagOp = null;
		List<TagOperation> tagOpList =  tagOperationRepository.findByEpcNew(epcNew);
		//
		if (tagOpList.size()!= 0) {
			TagOperation tagOp1 = tagOpList.get(0);
			if (tagOp1.getEpcWrited().booleanValue() && !tagOp1.getLocked().booleanValue()) {
				return tagOp1;
			}
		}
		return tagOp;
	}
	
	public boolean isEpcWrited(String epc) throws Exception {
		boolean ret = false;
		List<TagOperation> tagOpList = tagOperationRepository.findByEpcNew(epc);
		if (tagOpList.size() != 0) {
			ret = tagOpList.get(0).getEpcWrited().booleanValue();
		}
		return ret;
	}
	
	
	public void save(TagOperation tagOp) throws Exception {
		tagOperationRepository.save(tagOp);
	}

	
	public List<TagOperation> findByEpcOld(String epcOld) throws Exception {
		List<TagOperation> tagOpList = tagOperationRepository.findByEpcOld(epcOld);
		return tagOpList;
	}
	
	public boolean isTagWorkedByEpc(String epc) throws Exception {
		long cont = 0;
		boolean ret = false;
		long contNew = tagOperationRepository.countByEpcNew(epc);
		long contOld = tagOperationRepository.countByEpcOld(epc);
		cont = contNew + contOld;
		if (cont > 0) {
			ret = true;
		}
		return ret;
	}
	
	public boolean isTagWorkedByTid(String tid) throws Exception {
		boolean ret = false;
		long contTid = tagOperationRepository.countByTid(tid);
		if (contTid > 0) {
			ret = true;
		}
		return ret;
	}
	
	public TagOperation getTagByTid(String tid) throws Exception {
		TagOperation ret = null;
		List<TagOperation> listTagOperation = tagOperationRepository.findByTid(tid);
		if (listTagOperation.size() > 0) {
			ret = listTagOperation.get(0);
		}
		return ret;
	}

}