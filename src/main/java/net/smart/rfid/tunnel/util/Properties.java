package net.smart.rfid.tunnel.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class Properties implements InitializingBean {

	@Autowired
    Environment env;
	
	public static String ambiente;
	public static String anno;
	public static String oggetto;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties.ambiente = env.getProperty("ambiente");
		Properties.anno = env.getProperty("anno");
		Properties.oggetto = env.getProperty("oggetto");
		
	}


	

	

}
