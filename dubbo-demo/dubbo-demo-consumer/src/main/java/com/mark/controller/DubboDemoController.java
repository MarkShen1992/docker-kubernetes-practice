package com.mark.controller;

import com.mark.demo.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DubboDemoController {

    @Reference(version = "${demo.service.version}", url = "${demo.service.url}")
    private DemoService demoService;

    @RequestMapping(value = "/say-hello", method = GET)
    public String sayHello(@RequestParam("name") String name) {
        return demoService.sayHello(name);
    }
}
