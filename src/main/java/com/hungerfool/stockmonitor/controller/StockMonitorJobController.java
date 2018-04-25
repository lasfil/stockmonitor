package com.hungerfool.stockmonitor.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hungerfool.stockmonitor.domain.StockMonitor;
import com.hungerfool.stockmonitor.http.HttpService;
import com.hungerfool.stockmonitor.job.StockMonitorJob;
import com.hungerfool.stockmonitor.service.StockMonitorService;

@Controller
public class StockMonitorJobController {
	@Resource
	private HttpService httpAPIService;

	@Autowired
	StockMonitorService stockMonitorService;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/stockmonitor")
	public String stockMonitor(@RequestParam(value = "email") String email, Model model) {
		model.addAttribute("email", email);
		model.addAttribute("monitors", stockMonitorService.getStockMonitorByEmail(email));
		return "stockMonitor";
	}

	@PostMapping("/stockmonitor")
	public String addStock(@RequestParam(value = "stockCode") String stockCode,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "highThreshold", required = false) String highThreshold,
			@RequestParam(value = "lowThreshold", required = false) String lowThreshold, Model model)
			throws SchedulerException {
		StockMonitor monitor = null;
		try {
			monitor = stockMonitorService.createStockMonitor(stockCode, email,
					StringUtils.isEmpty(highThreshold) ? null : Double.parseDouble(highThreshold),
					StringUtils.isEmpty(lowThreshold) ? null : Double.parseDouble(lowThreshold));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (monitor == null) {
			return "redirect:stockmonitor?email=" + email;
		}

		// scheduler.start();
		JobDataMap jobData = new JobDataMap();
		jobData.put("stockCode", stockCode);
		jobData.put("email", email);
		JobDetail jobDetail = null;
		JobKey jobKey = new JobKey(email + ":" + stockCode, email);
		if (!scheduler.checkExists(jobKey)) {
			jobDetail = JobBuilder.newJob().ofType(StockMonitorJob.class).withIdentity(email + ":" + stockCode, email)
					.usingJobData(jobData).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(stockCode + ":" + email).startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
					.build();

			scheduler.scheduleJob(jobDetail, trigger);
		}
		model.addAttribute("email", email);
		model.addAttribute("monitors", stockMonitorService.getStockMonitorByEmail(email));
		return "redirect:stockmonitor?email=" + email;
	}

	@RequestMapping("/remove")
	public String removeStock(@RequestParam(value = "stockCode") String stockCode,
			@RequestParam(value = "email") String email, Model model) throws SchedulerException {

		JobKey jobKey = new JobKey(email + ":" + stockCode, email);
		if (scheduler.checkExists(jobKey)) {
			scheduler.deleteJob(jobKey);
			stockMonitorService.deleteMonitor(stockCode, email);
		}
		model.addAttribute("email", email);
		model.addAttribute("monitors", stockMonitorService.getStockMonitorByEmail(email));
		return "redirect:stockmonitor?email=" + email;
	}

}
