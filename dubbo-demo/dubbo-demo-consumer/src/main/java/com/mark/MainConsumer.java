package com.mark;

import com.mark.demo.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainConsumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-consumer.xml"});
        context.start();
        // Obtaining a remote service proxy
        DemoService demoService = (DemoService)context.getBean("demoService");
        // Executing remote methods
        String hello = demoService.sayHello("Mark");
        // Display the call result
        System.out.println(hello);
    }
}