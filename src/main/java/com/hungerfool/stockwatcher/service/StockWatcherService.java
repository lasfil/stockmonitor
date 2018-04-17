package com.hungerfool.stockwatcher.service;

import com.hungerfool.stockwatcher.domain.StockWatcher;

public interface StockWatcherService {


	public StockWatcher queryStockPrice(String stockCode, String email) throws Exception;

	public void checkNotification(StockWatcher watcher);

	public StockWatcher getStockWatcher(String stockCode, String email, Double highThreshold, Double lowThreshold);

	public void deleteWatcher(String stockCode, String email);

}
