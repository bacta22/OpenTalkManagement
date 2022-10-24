package com.ncc.asia.roadmap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanLifeCycleMainClass {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfigurationBeanLifeCycle.class);

        MyBeanForLifeCycle myBeanForLifeCycle1 = context.getBean(MyBeanForLifeCycle.class);
//        MyBeanForLifeCycle myBeanForLifeCycle2 = context.getBean(MyBeanForLifeCycle.class);

        myBeanForLifeCycle1.hello();

        context.close();

        // Bean Scope
//        System.out.println(myBeanForLifeCycle1);
//        System.out.println(myBeanForLifeCycle2);
    }

}
