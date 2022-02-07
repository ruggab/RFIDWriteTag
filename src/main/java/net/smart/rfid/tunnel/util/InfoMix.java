package net.smart.rfid.tunnel.util;

import org.apache.log4j.Logger;

import net.smart.rfid.tunnel.model.InfoPackage;

public class InfoMix extends InfoGeneral {
	Logger logger = Logger.getLogger(InfoMix.class);
	
	public InfoMix(InfoPackage infopackage) {
		super(infopackage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String createNewEpc(String currentEpc) {
		// Brand 6 bit (da 6 a 11)
		String secBrand = Utils.fromDecToBin(infoPackage.getBrand().toString());
		secBrand = Utils.padLeftZeros(secBrand, 6);
		
		// Seccion 2 bit da 12 a 13
		String secBin = Utils.fromDecToBin(infoPackage.getSection().toString());
		secBin = Utils.padLeftZeros(secBin, 2);
		// Sku 40 bit da 18 a 57
		String sku = infoPackage.getSku().substring(1, infoPackage.getSku().length() - 1);
		String skuBin = Utils.fromDecToBin(sku);
		skuBin = Utils.padLeftZeros(skuBin, 40);

		// Lotto-pack 17 bit da 97 a 113
		String lottoBin = Utils.fromDecToBin(infoPackage.getPack().toString());
		lottoBin = Utils.padLeftZeros(lottoBin, 17);
		
		// Converto in 128 bit l'epc
		String currentEpcBin = Utils.fromHexToBin(currentEpc);
		logger.debug(currentEpcBin);
		//
		StringBuilder builder = new StringBuilder(currentEpcBin);
		builder.replace(6, 12, secBrand);
		builder.replace(12, 14, secBin);
		builder.replace(18, 58, skuBin);
		builder.replace(97, 114, lottoBin);
		//
		
		logger.debug(builder.toString());
		//
		String ret = Utils.fromBinToHex(builder.toString()).toUpperCase();
		return ret;
	}

	@Override
	public String createPasswordUnlock(String currentEpc) {
		return infoPackage.getPassword();
	}

	@Override
	public String createPasswordlock(String currentEpc) {
		return infoPackage.getPassword();
	}

}
