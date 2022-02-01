package net.smart.rfid.tunnel.util;

import java.math.BigInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

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

	public static String fromHexToInt(String hex) {
		String ret = hex.substring(18, hex.length());
		int ret1 = Integer.parseInt(ret, 16);
		return ret1 + "";

	}

	public static void main(String[] args) {
		String ret = fromHexToBin("FFFFAAAA000011112222333344445555");
		System.out.println(ret);
		
		//String aa = getSerialFromMask2("FFFA-----","FFFBA12345");
		
		//System.out.println(aa);
		
		
		//http://localhost:8080/api/v1/callWMSIn
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("http://localhost:8080/api/v1/callWMSIn");
//		String response = target.request(MediaType.APPLICATION_JSON).post(Entity.json("ffff"), String.class);
//		
//		System.out.println(response);
		  
	}
	//XXXXX----XXXX sostituisce i ----- con i valori corrispondenti
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
		String str = String. valueOf(maskArr);
		String ret = str.replace("X", "");
		return ret;
	}
	
	//XXXXX----XXXX se la string contiene i valori corrispondenti a X restituisce quelli corrispondenti ai ----
	public static String getSerialFromMask2(String mask, String epc) {
		char splitter = '-';
		char[] maskArr = mask.toCharArray();
		char[] epcArr = epc.toCharArray();
		String barcode = "";
		for (int i = 0; i < maskArr.length; i++) {
			if (maskArr[i] == splitter ||  maskArr[i] == epcArr[i]) {
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
		String ret = st.replaceAll("\\s+","");;
		return ret;
	}
	
	public static String fromHexToBin(String hex) {
		
		BigInteger bigInt = new BigInteger(hex, 16);
		String binary = "";//Integer.toBinaryString(num);
		return binary;
	}

}
