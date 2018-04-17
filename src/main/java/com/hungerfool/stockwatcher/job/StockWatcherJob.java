package com.hungerfool.stockwatcher.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.hungerfool.stockwatcher.domain.StockWatcher;
import com.hungerfool.stockwatcher.service.StockWatcherService;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StockWatcherJob implements Job {

	private StockWatcher watcher;

	@Autowired
	StockWatcherService stockWatcherService;

	public void setWatcher(StockWatcher watcher) {
		this.watcher = watcher;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		try {
			 System.out.println(watcher.getStockName() + " : " + watcher.getStockCode() +
			 " : "
			 + watcher.getLastQueryTime().getTime().toString() + " : " +
			 watcher.getCurrentPrice());
			stockWatcherService.queryStockPrice(watcher);
			stockWatcherService.checkNotification(watcher);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(context.getJobDetail().getKey());
		}

	}

}
