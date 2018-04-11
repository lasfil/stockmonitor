package com.hungerfool.stockwatcher.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StockWatcherJob implements Job{
	private double maxPrice;
	private double minPrice;
	private String stockCode;
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private String email;
	
	
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
	}
}
