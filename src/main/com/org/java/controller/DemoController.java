package org.java.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author Administrator
 */
@RestController(value = "demo")
@Slf4j
public class DemoController {


    @Autowired
    private ThreadPoolTaskExecutor taskScheduler;


    @RequestMapping(value = {"demo1"}, method = RequestMethod.POST)
    @Async
    @Scheduled(cron = "5 * * * * ?")
    public CompletableFuture<String> demo() {
        taskScheduler.submit(this::demo1);
        taskScheduler.submit(this::demo2);
        taskScheduler.submit(this::demo3);
        return CompletableFuture.completedFuture("yes");
    }

    void demo1() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("1------------>");
    }

    void demo2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("2------------>");
    }

    void demo3() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("3------------>");
    }
}
