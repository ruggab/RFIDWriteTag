package net.smart.rfid.tunnel.util;

import net.smart.rfid.tunnel.model.InfoPackage;

public class InfoTest extends InfoGeneral {

	public InfoTest(InfoPackage infopackage) {
		super(infopackage);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String createNewEpc(String currentEpc){
		return infoPackage.getPack() + currentEpc.substring(5, currentEpc.length());
	}
	
	@Override
	public String createPasswordUnlock(String currentEpc){
		return infoPackage.getPassword();
	}
	
	@Override
	public String createPasswordlock(String currentEpc){
		return infoPackage.getPassword();
	}
	

}
