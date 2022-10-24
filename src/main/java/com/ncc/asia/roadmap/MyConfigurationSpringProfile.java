package com.ncc.asia.roadmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
             @PropertySource("classpath:info.properties"),
                @PropertySource("classpath:anotherInfo.properties")

})
public class MyConfigurationSpringProfile {

    @Autowired
    private Environment environment;

    @Value("${myCompany.nameInfo}")
    private String companyName;
    // private static String companyName; => can not bind to static field

    @Bean
    public MyCompany myDataSource( @Value("${myCompany.name}") String name,
                                   @Value("${myCompany.locationInfo}") String location) {
        MyCompany myCompany = new MyCompany();
        myCompany.setName(name);
        myCompany.setLocation(location);

        System.out.println("Name: " + myCompany.getName() + " - Location: " + myCompany.getLocation());

        /*
        System.out.println("Environment active : " );
        for (String profileName : environment.getActiveProfiles()) {
            System.out.println(profileName);
        }
        System.out.println("Environment : " + environment.getProperty("myCompany.name"));
        System.out.println("Environment : " + environment.getProperty("myCompany.location"));
        */

        return myCompany;
    }

    @Bean
    @Profile("dev")
    public MyBeanForDev myBeanForDev() {
        System.out.println("Create Bean for Dev");
        return new MyBeanForDev();
    }

    @Bean
    @Profile("qa")
    public MyBeanForQa myBeanForQa() {
        System.out.println("Create Bean for Qa");
        return new MyBeanForQa();
    }
}

// value in application.properties will be auto-detected
// the same key in many properties file => The priority is : inject value from application.properties
// will be many file properties with same key => inject value from file last declared

/*
* The priortity when we set value for one property both : Environment variable, Argument program, Profile
(Example: set the comp name in argument, environment, profile active)
	Argument variable > Environment variable > Profile active > application,properties
 */
