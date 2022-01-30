package net.smart.rfid.tunnel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.smart.rfid.tunnel.job.WriteEpc;

@SpringBootTest
class RfidWriteApplicationTests {

	@DisplayName("Test Spring @Autowired Integration")
	@Test
	void contextLoads() throws Exception {
		
		//
		WriteEpc epcWriter = new WriteEpc();
		epcWriter.run();

	}

}
