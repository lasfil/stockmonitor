package com.hungerfool.stockmonitor.job;

import java.util.Calendar;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TimeJob implements Job {

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("JobDataMap time: " + context.getJobDetail().getJobDataMap().get("time"));
		System.out.println("TimeJob time: " + time);
		context.getJobDetail().getJobDataMap().put("time", Calendar.getInstance().getTime().toString());
	}

}
