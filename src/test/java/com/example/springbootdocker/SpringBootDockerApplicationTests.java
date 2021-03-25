package com.example.springbootdocker;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class SpringBootDockerApplicationTests {

	@Test
	void contextLoads() {
		log.info("some test done");
	}

}
