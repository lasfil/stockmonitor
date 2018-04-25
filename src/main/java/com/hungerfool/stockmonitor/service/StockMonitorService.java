package com.hungerfool.stockmonitor.service;

import java.util.List;

import com.hungerfool.stockmonitor.domain.StockMonitor;

public interface StockMonitorService {

	public StockMonitor queryStockPrice(String stockCode, String email) throws Exception;

	public void checkNotification(StockMonitor monitor);

	public StockMonitor saveStockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold);

	public void deleteMonitor(String stockCode, String email);

	public List<StockMonitor> getStockMonitorByEmail(String email);

}
