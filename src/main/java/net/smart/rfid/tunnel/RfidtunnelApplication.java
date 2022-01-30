package net.smart.rfid.tunnel;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;




@SpringBootApplication
public class RfidtunnelApplication extends SpringBootServletInitializer {
	
	Logger logger = Logger.getLogger(RfidtunnelApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(RfidtunnelApplication.class, args);
		//logger.info("RfidtunnelApplication OK");
		Logger.getLogger(RfidtunnelApplication.class).info("RfidtunnelApplication OK");
       
	}
	
}
