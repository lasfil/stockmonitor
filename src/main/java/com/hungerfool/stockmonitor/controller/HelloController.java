package com.hungerfool.stockmonitor.controller;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hungerfool.stockmonitor.job.TimeJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */
@Controller
public class HelloController {
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@RequestMapping(value = "/index")
	public String index(Model model) {
		Person single = new Person("hyj", 21);
		List<Person> people = new ArrayList<Person>();
		Person p1 = new Person("dlp", 21);
		Person p2 = new Person("tq", 21);
		Person p3 = new Person("mk", 21);
		people.add(p1);
		people.add(p2);
		people.add(p3);
		model.addAttribute("singlePerson", single);
		model.addAttribute("people", people);
		return "index";
	}

	@RequestMapping("/time")
	public String time() throws SchedulerException {
		scheduler.start();
		JobDataMap jobData = new JobDataMap();
		Date now = new Date();
		jobData.put("time", now.toString());
		JobDetail jobDetail = JobBuilder.newJob().ofType(TimeJob.class).withIdentity("time").usingJobData(jobData)
				.build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(now.toString()).startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		scheduler.scheduleJob(jobDetail, trigger);
		return "time";
	}
}

class Person {
	private String name;
	private Integer age;

	public Person() {
		super();
	}

	public Person(String name, Integer gae) {
		super();
		this.name = name;
		this.age = gae;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public Integer setAge(Integer age) {
		return age;
	}

}