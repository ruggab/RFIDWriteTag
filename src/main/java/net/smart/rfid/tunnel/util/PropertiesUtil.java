package net.smart.rfid.tunnel.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rfid.write")
@Configuration
public class PropertiesUtil {
	private static String hostname;
	
	private static String targetEpc;
	
	
	public static String getHostname() {
		return hostname;
	}

	public  void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public static String getTargetEpc() {
		return targetEpc;
	}

	public  void setTargetEpc(String targetEpc) {
		this.targetEpc = targetEpc;
	}
	
	

}
