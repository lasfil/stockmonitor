package com.hungerfool.stockwatcher.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.hungerfool.stockwatcher.http.HttpService;

@PersistJobDataAfterExecution
public class StockWatcherJob implements Job {
	private double maxPrice;
	private double minPrice;
	private String stockCode;
	@Resource
	private HttpService httpAPIService;

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
		try {
			JobDataMap dataMap = context.getMergedJobDataMap();

			String str = httpAPIService.doGet("http://hq.sinajs.cn/list=" + stockCode);
			System.out.println(str);
		} catch (Exception e) {

		}
	}
}
