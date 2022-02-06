package net.smart.rfid.tunnel.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.GpoMode;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.SearchMode;

public class Utils {

	public static final String MISSING_EXPECTED = "E-MISSING-EXPECTED";
	public static final String MISSING_PACKAGE = "E-MISSING-PACKAGE";
	public static final String MORE_PACKAGE = "E-MORE-PACKAGE";
	public static final String TAG_NO_READ = "E-TAG-NO-READ";
	public static final String OK = "OK";
	public static final String KO = "KO";
	public static final String EPC = "EPC";
	public static final String TID = "TID";
	public static final String SKU = "SKU";
	public static final String QTY = "QTY";
	public static final String TAG_ERR = "E-";

	public static AutoStartMode getAutoStartMode(Integer idAutoStartMode) throws OctaneSdkException {

		AutoStartMode ret = null;
		switch (idAutoStartMode) {
		case 1:
			ret = AutoStartMode.GpiTrigger;
			break;
		case 2:
			ret = AutoStartMode.Immediate;
			break;
		case 3:
			ret = AutoStartMode.Periodic;
			break;
		default:
			ret = AutoStartMode.GpiTrigger;
			break;
		}
		return ret;
	}

	public static AutoStopMode getAutoStopMode(Integer idAutoStopMode) throws OctaneSdkException {
		AutoStopMode ret = null;

		switch (idAutoStopMode) {
		case 1:
			ret = AutoStopMode.GpiTrigger;
			break;
		case 2:
			ret = AutoStopMode.Duration;
			break;
		case 3:
			ret = AutoStopMode.None;
			break;
		default:
			ret = AutoStopMode.GpiTrigger;
			break;
		}
		return ret;
	}

	public static GpoMode getGpoMode(Long idGpoMode) throws OctaneSdkException {
		GpoMode ret = null;

		switch (idGpoMode.intValue()) {
		case 1:
			ret = GpoMode.Normal;
			break;
		case 2:
			ret = GpoMode.Pulsed;
			break;
		case 3:
			ret = GpoMode.ReaderInventoryStatus;
			break;
		case 4:
			ret = GpoMode.LLRPConnectionStatus;
			break;
		default:
			ret = GpoMode.Normal;
			break;
		}
		return ret;
	}

	public static SearchMode getSearchMode(Integer idSearchMode) throws OctaneSdkException {

		SearchMode ret = null;
		switch (idSearchMode) {
		case 1:
			ret = SearchMode.ReaderSelected;
			break;
		case 2:
			ret = SearchMode.SingleTarget;
			break;
		case 3:
			ret = SearchMode.SingleTargetReset;
			break;
		case 4:
			ret = SearchMode.TagFocus;
			break;
		case 5:
			ret = SearchMode.DualTarget;
			break;
		case 6:
			ret = SearchMode.DualTargetBtoASelect;
			break;
		default:
			ret = SearchMode.SingleTarget;
			break;
		}
		return ret;
	}
	
	public static String padLeftZeros(String inputString, int length) {
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    while (sb.length() < length - inputString.length()) {
	        sb.append('0');
	    }
	    sb.append(inputString);

	    return sb.toString();
	}

	public static String fromHexToInt(String hex) {
		String ret = hex.substring(18, hex.length());
		int ret1 = Integer.parseInt(ret, 16);
		return ret1 + "";

	}

	public static void main(String[] args) {
		//
//		String currentEpc = "3035EBD2F8143D6F6AA5EBDBAAAA3333";
//		String bynaryEpc = Utils.fromHexToBin(currentEpc);
//		//Etraggo il serial number per ricavare la password
//		String serialNumber = bynaryEpc.substring(64, 96);
//		//Trasformo il serial number da Bin a decimal
//		Integer serialNumDec = Utils.fromBinToDecimal(serialNumber);
//		Integer chiave = Utils.MD5(serialNumDec);
//		System.out.println(chiave);
		
//		String secBrand = Utils.fromDecToBin("1");
//		int numZeri = 6 - secBrand.length();
//		String newSecBrand = String.format("%05d", Integer.parseInt(secBrand));
//		System.out.println(newSecBrand);
		
		String skuBin = "11111111111111111111111111111111111111";
		
		String skuBinNew = padLeftZeros(skuBin, 40);
		System.out.println(skuBinNew);
	}

	// XXXXX----XXXX sostituisce i ----- con i valori corrispondenti
	public static String getSerialFromMask(String mask, String epc) {
		char splitter = 'X';
		char[] maskArr = mask.toCharArray();
		char[] textArr = epc.toCharArray();
		int textI = 0;
		for (int i = 0; i < maskArr.length; i++) {
			if (maskArr[i] != splitter) {

				if (maskArr[i] == '-' && textI < textArr.length) {
					maskArr[i] = textArr[textI];
				}
				textI++;
			}
		}
		String str = String.valueOf(maskArr);
		String ret = str.replace("X", "");
		return ret;
	}

	// XXXXX----XXXX se la string contiene i valori corrispondenti a X restituisce quelli corrispondenti ai ----
	public static String getSerialFromMask2(String mask, String epc) {
		char splitter = '-';
		char[] maskArr = mask.toCharArray();
		char[] epcArr = epc.toCharArray();
		String barcode = "";
		for (int i = 0; i < maskArr.length; i++) {
			if (maskArr[i] == splitter || maskArr[i] == epcArr[i]) {
				if (maskArr[i] == '-') {
					barcode = barcode + epcArr[i];
				}
			} else {
				return "";
			}
		}
		return barcode;
	}

	public static String removeSpaces(String st) {
		String ret = st.replaceAll("\\s+", "");
		return ret;
	}

	public static String fromHexToBin(String hex) {
		BigInteger bigInt = new BigInteger(hex, 16);
		String binary = bigInt.toString(2);
		return binary;
	}
	
	public static String fromBinToHex(String bin) {
		BigInteger bigInt = new BigInteger(bin, 2);
		String base16 = bigInt.toString(16);
		return base16;
	}
	
	public static String fromDecToHex(String dec) {
		BigInteger bigInt = new BigInteger(dec, 10);
		String base16 = bigInt.toString(16);
		return base16;
	}
	

	public static Integer MD5(Integer value) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

		}
		//
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt((int) value & 0xFFFFFFFF);
		byte[] theDigest = md.digest(bb.array());

		//
		ByteBuffer buffer = ByteBuffer.wrap(theDigest, 0, 4);
		buffer.order(ByteOrder.BIG_ENDIAN);
		return buffer.getInt();
	}
	
	public static Integer fromBinToDecimal(String bin) {
		BigInteger bigInt = new BigInteger(bin, 2);
		return bigInt.intValue();
	}
	
	public static String fromDecToBin(String dec) {
		BigInteger bigInt = new BigInteger(dec, 10);
		String binary = bigInt.toString(2);
		return binary;
	}
	
	

}
