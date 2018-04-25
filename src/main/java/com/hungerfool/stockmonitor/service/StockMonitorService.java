package com.hungerfool.stockmonitor.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.hungerfool.stockmonitor.domain.StockMonitor;

public interface StockMonitorService {

	public StockMonitor queryStockPrice(String stockCode, String email) throws Exception;

	public void checkNotification(StockMonitor monitor);

	public StockMonitor getStockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold);

	public void deleteMonitor(String stockCode, String email);

	public List<StockMonitor> getStockMonitorByEmail(String email);

	public StockMonitor createStockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold)
			throws ClientProtocolException, IOException;

}
