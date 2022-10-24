package com.ncc.asia;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenTalkManagementWebApplication {


	public static void main(String[] args) {
		SpringApplication.run(OpenTalkManagementWebApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run (MyClassForInjectValueTest myClassForInjectValueTest) {
//		return args -> {
//			System.out.println("Name is : " + myClassForInjectValueTest.getName());
//			System.out.println("Location is : " + myClassForInjectValueTest.getLocation());
//			System.out.println("Branch is : " + myClassForInjectValueTest.getBranch());
//		};
//	}
}



// @SpringBootApplication:  auto-configuration and component scanning.
// A single @SpringBootApplication annotation can be used to enable @ComponentScan:
// @ComponentScan: enable @Component scan on the package where the application is located