package net.smart.rfid.tunnel.util;

public class PackageCSV {

	//public static final String[] PackageCSV = { "tunnel", "packageData", "quantity", "expected", "readingDate" };

	private String tunnel;
	private String packageData;
	private String quantity;
	private String expected;
	private String readingDate;

	public String getPackageData() {
		return packageData;
	}

	public void setPackageData(String packageData) {
		this.packageData = packageData;
	}

	public String getTunnel() {
		return tunnel;
	}

	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getReadingDate() {
		return readingDate;
	}

	public void setReadingDate(String readingDate) {
		this.readingDate = readingDate;
	}


}
