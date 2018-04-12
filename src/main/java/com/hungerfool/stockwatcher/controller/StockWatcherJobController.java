package com.hungerfool.stockwatcher.controller;

import javax.annotation.Resource;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hungerfool.stockwatcher.http.HttpService;
import com.hungerfool.stockwatcher.job.StockWatcherJob;

@RestController
@RequestMapping(value = "/job")
public class StockWatcherJobController {
	@Resource
	private HttpService httpAPIService;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@RequestMapping("/httpclient")
	public String test() throws Exception {
		String str = httpAPIService.doGet("http://hq.sinajs.cn/list=sh601006,sh600000");
		System.out.println(str);
		return "hello";
	}

	@RequestMapping("/addStock")
	public String addStock() throws SchedulerException {
		// 启动调度器
		scheduler.start();
//		JobDetail jobDetail = JobBuilder.newJob().ofType(StockWatcherJob.class) // 引用Job Class
//				.withIdentity("sh601006", "group1") // 设置name/group
//				.usingJobData("stockCode", "sh601006") // 加入属性到ageJobDataMap
//				.usingJobData("email", "lasfil@163.com") // 加入属性到ageJobDataMap
//				.build();
//
//		// 定义一个每秒执行一次的SimpleTrigger
//		Trigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity("trigger1")
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
//
//		
//
//		try {
//			scheduler.scheduleJob(jobDetail, trigger);
//
//		} catch (SchedulerException e) {
//
//		}
		return "hello";
	}
}
