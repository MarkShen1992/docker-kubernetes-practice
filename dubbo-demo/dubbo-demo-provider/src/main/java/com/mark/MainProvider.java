package com.mark;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MainProvider {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MainProvider.class).run(args);
    }
}
