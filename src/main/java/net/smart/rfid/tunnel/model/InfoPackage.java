package net.smart.rfid.tunnel.model;

public class InfoPackage {
	private String pswLock;
	private String pswUnlock;

	private String sku;

	private Integer pack;

	private Integer section;

	private Integer brand;

	public InfoPackage() {
	}

	public InfoPackage(String sku, Integer pack, Integer brand, Integer section, String pswLock, String pswUnlock) {
		this.sku = sku;
		this.pack = pack;
		this.brand = brand;
		this.section = section;
		this.pswLock = pswLock;
		this.pswUnlock = pswUnlock;

	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getPack() {
		return pack;
	}

	public void setPack(Integer pack) {
		this.pack = pack;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public Integer getBrand() {
		return brand;
	}

	public void setBrand(Integer brand) {
		this.brand = brand;
	}

	public String getPswLock() {
		return pswLock;
	}

	public void setPswLock(String pswLock) {
		this.pswLock = pswLock;
	}

	public String getPswUnlock() {
		return pswUnlock;
	}

	public void setPswUnlock(String pswUnlock) {
		this.pswUnlock = pswUnlock;
	}

}
