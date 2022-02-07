package net.smart.rfid.tunnel.util;

import net.smart.rfid.tunnel.model.InfoPackage;

public class InfoGeneral {
    protected InfoPackage infoPackage;
	
	public InfoGeneral(InfoPackage infopackage) {
		this.infoPackage = infopackage;
	}
	
	public String createNewEpc(String currentEpc){
		return null;
	}
	
	public String createPasswordUnlock(String currentEpc){
		return null;
	}
	
	public String createPasswordlock(String currentEpc){
		return null;
	}

}
