package com.ncc.asia.roadmap;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:info.properties")
public class MyClassForInjectValueTest {
    private String name;
    private String location;


    @Value("${myBranch}")
    private String branch; // field

    // constructor
    public MyClassForInjectValueTest (@Value("${myCompany.name}") String name) { // constructor
        this.name = name;
    }

    // setter
    @Autowired
    public void setLocation(@Value("${myCompany.location}") String location) { // setter
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getBranch() {
        return branch;
    }

}

// Priority: Active profile > application.properties > @PropertySource (in @Configuration class) > @PropertySource class