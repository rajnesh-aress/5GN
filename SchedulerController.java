/*package com.cmdb.integration.controller;

import com.cmdb.integration.SchedulerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    private SchedulerConfiguration schedulerConfiguration;

    @GetMapping(value = "/stop")
    public String stopSchedule() {
        postProcessor.postProcessBeforeDestruction(schedulerConfiguration, "");
        return "OK";
    }

    @GetMapping(value = "/start")
    public String startSchedule() {
        postProcessor.postProcessAfterInitialization(schedulerConfiguration, "");
        return "OK";
    }
}
*/