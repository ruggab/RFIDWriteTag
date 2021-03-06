package net.smart.rfid.tunnel.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "tag_operation")
public class TagOperation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String tid;

	private String epcOld;

	private String epcNew;

	private Boolean epcWrited;
	
	private Boolean pswWrited;

	private Boolean locked;
	
	private Boolean unlocked;

	private Integer numAntenna;

	private Integer idOperation;

	private Integer seqOperation;

	public TagOperation() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEpcOld() {
		return epcOld;
	}

	public void setEpcOld(String epcOld) {
		this.epcOld = epcOld;
	}

	public String getEpcNew() {
		return epcNew;
	}

	public void setEpcNew(String epcNew) {
		this.epcNew = epcNew;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}



	public Boolean getEpcWrited() {
		return epcWrited;
	}

	public void setEpcWrited(Boolean epcWrited) {
		this.epcWrited = epcWrited;
	}

	public int getNumAntenna() {
		return numAntenna;
	}

	public void setNumAntenna(int numAntenna) {
		this.numAntenna = numAntenna;
	}

	public int getIdOperation() {
		return idOperation;
	}

	public void setIdOperation(int idOperation) {
		this.idOperation = idOperation;
	}

	public Integer getSeqOperation() {
		return seqOperation;
	}

	public void setSeqOperation(Integer seqOperation) {
		this.seqOperation = seqOperation;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Boolean getPswWrited() {
		return pswWrited;
	}

	public void setPswWrited(Boolean pswWrited) {
		this.pswWrited = pswWrited;
	}

	public Boolean getUnlocked() {
		return unlocked;
	}

	public void setUnlocked(Boolean unlocked) {
		this.unlocked = unlocked;
	}
	
	

}
