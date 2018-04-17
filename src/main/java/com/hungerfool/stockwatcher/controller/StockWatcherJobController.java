package com.hungerfool.stockwatcher.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hungerfool.stockwatcher.domain.StockWatcher;
import com.hungerfool.stockwatcher.http.HttpService;
import com.hungerfool.stockwatcher.job.StockWatcherJob;
import com.hungerfool.stockwatcher.job.TimeJob;
import com.hungerfool.stockwatcher.service.StockWatcherService;

@RestController
@RequestMapping(value = "/job")
public class StockWatcherJobController {
	@Resource
	private HttpService httpAPIService;

	@Autowired
	StockWatcherService stockWatcherService;

	@RequestMapping("/httpclient")
	public String test() throws Exception {
		String str = httpAPIService.doGet("http://hq.sinajs.cn/list=sh601006,sh600000");
		System.out.println(str);
		return "hello";
	}

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@RequestMapping("/addwatcher")
	public String addStock(@RequestParam(value = "stockCode") String stockCode,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "highThreshold", required = false) String highThreshold,
			@RequestParam(value = "lowThreshold", required = false) String lowThreshold) throws SchedulerException {
		StockWatcher watcher = stockWatcherService.getStockWatcher(stockCode, email,
				StringUtils.isEmpty(highThreshold) ? null : Double.parseDouble(highThreshold),
				StringUtils.isEmpty(lowThreshold) ? null : Double.parseDouble(lowThreshold));

		//scheduler.start();
		JobDataMap jobData = new JobDataMap();
		jobData.put("stockCode", stockCode);
		jobData.put("email", email);
		JobDetail jobDetail = null;
		JobKey jobKey = new JobKey(email + ":" + stockCode, email);
		if (!scheduler.checkExists(jobKey)) {
			jobDetail = JobBuilder.newJob().ofType(StockWatcherJob.class).withIdentity(email + ":" + stockCode, email)
					.usingJobData(jobData).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(stockCode + ":" + email).startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

			scheduler.scheduleJob(jobDetail, trigger);
		}
		

		return watcher.getStockCode() + watcher.getEmail();
	}
	
	@RequestMapping("/removewatcher")
	public String addStock(@RequestParam(value = "stockCode") String stockCode,
			@RequestParam(value = "email") String email) throws SchedulerException {
		
		JobKey jobKey = new JobKey(email + ":" + stockCode, email);
		if (scheduler.checkExists(jobKey)) {
			scheduler.deleteJob(jobKey);
			stockWatcherService.deleteWatcher(stockCode, email);
		}
		return stockCode + email + " removed";
	}

	@RequestMapping("/time")
	public String time() throws SchedulerException {
		scheduler.start();
		JobDataMap jobData = new JobDataMap();
		Date now = new Date();
		jobData.put("time", now.toString());
		JobDetail jobDetail = JobBuilder.newJob().ofType(TimeJob.class).withIdentity(now.toString())
				.usingJobData(jobData).build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(now.toString()).startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		scheduler.scheduleJob(jobDetail, trigger);
		return "time";
	}

}
