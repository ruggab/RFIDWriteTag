package net.smart.rfid.tunnel.model;

public class ConfTunnel {

	private Integer dbmAntenna1;
	private Integer dbmAntenna2;
	private Integer dbmAntenna3;
	private Integer dbmAntenna4;

	public ConfTunnel() {
	}

	public ConfTunnel(Integer dbmAntenna1, Integer dbmAntenna2, Integer dbmAntenna3, Integer dbmAntenna4) {
		this.dbmAntenna1 = dbmAntenna1;
		this.dbmAntenna2 = dbmAntenna2;
		this.dbmAntenna3 = dbmAntenna3;
		this.dbmAntenna4 = dbmAntenna4;

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

}
