package net.smart.rfid.tunnel.util;

import net.smart.rfid.tunnel.model.InfoPackage;

public class InfoGeneratorFactory {

	public static InfoGenerator createInfoGenerator(InfoPackage infoPackage) {
		InfoGenerator infoGenerator = new InfoGenerator();
		InfoGeneral infoGeneral = null;
		if (PropertiesUtil.getInfoClient().equals("test")) {
			infoGeneral = new InfoTest(infoPackage);
		}
		if (PropertiesUtil.getInfoClient().equals("zara")) {
			infoGeneral = new InfoZara(infoPackage);
		}
		if (PropertiesUtil.getInfoClient().equals("mix")) {
			infoGeneral = new InfoMix(infoPackage);
		}
		infoGenerator.setInfo(infoGeneral);
		return infoGenerator;
	}
	
	
	
	
}
