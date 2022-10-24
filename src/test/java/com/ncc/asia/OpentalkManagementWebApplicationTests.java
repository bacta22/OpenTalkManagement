package com.ncc.asia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpentalkManagementWebApplicationTests {

	@Value("${myCompany.name}")
	public String name;
	@Test
	void contextLoads() {
		System.out.println(name);
	}

}
