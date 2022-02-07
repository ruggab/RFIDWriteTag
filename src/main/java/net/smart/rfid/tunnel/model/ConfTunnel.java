package net.smart.rfid.tunnel.model;

public class ConfTunnel {

	private Integer dbmAntenna1;
	private Integer dbmAntenna2;
	private Integer dbmAntenna3;
	private Integer dbmAntenna4;
	
	private Boolean antenna1Enable;
	private Boolean antenna2Enable;
	private Boolean antenna3Enable;

	public ConfTunnel() {
	}

	public ConfTunnel(Integer dbmAntenna1, Integer dbmAntenna2, Integer dbmAntenna3, Integer dbmAntenna4, Boolean antenna1Enable, Boolean antenna2Enable, Boolean antenna3Enable) {
		this.dbmAntenna1 = dbmAntenna1;
		this.dbmAntenna2 = dbmAntenna2;
		this.dbmAntenna3 = dbmAntenna3;
		this.dbmAntenna4 = dbmAntenna4;
		this.antenna1Enable = antenna1Enable;
		this.antenna2Enable = antenna2Enable;
		this.antenna3Enable = antenna3Enable;
	}

	public Integer getDbmAntenna1() {
		return dbmAntenna1;
	}

	public void setDbmAntenna1(Integer dbmAntenna1) {
		this.dbmAntenna1 = dbmAntenna1;
	}

	public Integer getDbmAntenna2() {
		return dbmAntenna2;
	}

	public void setDbmAntenna2(Integer dbmAntenna2) {
		this.dbmAntenna2 = dbmAntenna2;
	}

	public Integer getDbmAntenna3() {
		return dbmAntenna3;
	}

	public void setDbmAntenna3(Integer dbmAntenna3) {
		this.dbmAntenna3 = dbmAntenna3;
	}

	public Integer getDbmAntenna4() {
		return dbmAntenna4;
	}

	public void setDbmAntenna4(Integer dbmAntenna4) {
		this.dbmAntenna4 = dbmAntenna4;
	}

	public Boolean getAntenna1Enable() {
		return antenna1Enable;
	}

	public void setAntenna1Enable(Boolean antenna1Enable) {
		this.antenna1Enable = antenna1Enable;
	}

	public Boolean getAntenna2Enable() {
		return antenna2Enable;
	}

	public void setAntenna2Enable(Boolean antenna2Enable) {
		this.antenna2Enable = antenna2Enable;
	}

	public Boolean getAntenna3Enable() {
		return antenna3Enable;
	}

	public void setAntenna3Enable(Boolean antenna3Enable) {
		this.antenna3Enable = antenna3Enable;
	}


	
	

}
