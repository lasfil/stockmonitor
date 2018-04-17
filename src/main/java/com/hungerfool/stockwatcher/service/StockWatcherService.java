package com.hungerfool.stockwatcher.service;

import com.hungerfool.stockwatcher.domain.StockWatcher;

public interface StockWatcherService {


	public void queryStockPrice(StockWatcher watcher) throws Exception;

	public void checkNotification(StockWatcher watcher);

	public StockWatcher getStockWatcher(String stockCode, String email, Double highThreshold, Double lowThreshold);
}
