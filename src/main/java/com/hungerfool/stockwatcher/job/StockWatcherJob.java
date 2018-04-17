package com.hungerfool.stockwatcher.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hungerfool.stockwatcher.service.StockWatcherService;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StockWatcherJob implements Job {

	private String stockCode;

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;
	@Autowired
	StockWatcherService stockWatcherService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		try {
			stockWatcherService.checkNotification(stockWatcherService.queryStockPrice(stockCode, email));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(context.getJobDetail().getKey());
		}

	}

}
