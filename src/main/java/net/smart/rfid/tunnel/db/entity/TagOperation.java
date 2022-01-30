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

	private String epcOld;

	private String epcNew;

	private Boolean writed;

	private Boolean locked;

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

	public Boolean getWrited() {
		return writed;
	}

	public void setWrited(Boolean writed) {
		this.writed = writed;
	}

}
