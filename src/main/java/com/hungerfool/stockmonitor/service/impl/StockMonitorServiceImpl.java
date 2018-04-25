package com.hungerfool.stockmonitor.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hungerfool.stockmonitor.dao.impl.StockMonitorRepository;
import com.hungerfool.stockmonitor.domain.StockMonitor;
import com.hungerfool.stockmonitor.email.EmailService;
import com.hungerfool.stockmonitor.http.HttpService;
import com.hungerfool.stockmonitor.service.StockMonitorService;

@Service
public class StockMonitorServiceImpl implements StockMonitorService {
	@Autowired
	StockMonitorRepository stockMonitorRepository;

	@Autowired
	private HttpService httpService;

	@Autowired
	private EmailService emailService;

	SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

	public static String emailTitle = "您设置的股票价格发生变化";

	@Override
	public StockMonitor queryStockPrice(String stockCode, String email) throws ClientProtocolException, IOException {
		
		String stockString = httpService.doGet("http://hq.sinajs.cn/list=" + stockCode);
		// System.out.println(stockInfo);
		stockString = stockString.split("=")[1].replaceAll("\"", "").replace(";", "").trim();
		if (StringUtils.isEmpty(stockString)) {
			return null;
		}
		String[] stockInfo = stockString.split(",");
		StockMonitor monitor = stockMonitorRepository.findByStockCodeAndEmail(stockCode, email);
		monitor.setCurrentPrice(Double.parseDouble(stockInfo[3]));
		monitor.setStockName(stockInfo[0]);
		monitor.setLastQueryTime(Calendar.getInstance());
		stockMonitorRepository.saveAndFlush(monitor);
		return monitor;
	}

	@Override
	public StockMonitor getStockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold) {
		StockMonitor monitor = stockMonitorRepository.findByStockCodeAndEmail(stockCode, email);
		if (monitor == null) {
			monitor = stockMonitorRepository.save(new StockMonitor(stockCode, email, highThreshold, lowThreshold));
		} 
		return monitor;

	}

	@Override
	public void checkNotification(StockMonitor monitor) {
		if (monitor == null) {
			return;
		}

		if ((monitor.getLowThreshold() != null && monitor.getCurrentPrice() > monitor.getLowThreshold())
				&& (monitor.getHighThreshold() != null && monitor.getCurrentPrice() < monitor.getHighThreshold())) {
			return;
		}
		StringBuilder content = new StringBuilder();
		content.append(sdf.format(monitor.getLastQueryTime().getTime())).append("[").append(monitor.getStockName())
				.append("]").append("目前价格").append(monitor.getCurrentPrice());
		if (monitor.getHighThreshold() != null && monitor.getCurrentPrice() > monitor.getHighThreshold()) {
			content.append("已高于高位提醒价").append(monitor.getHighThreshold());
		}

		if (monitor.getLowThreshold() != null && monitor.getCurrentPrice() < monitor.getLowThreshold()) {
			content.append("已低于低位提醒价").append(monitor.getLowThreshold());
		}

		 System.out.println(content);
		// emailService.sendSimpleMail(monitor.getEmail(), emailTitle,
		// content.toString());

	}

	@Override
	public void deleteMonitor(String stockCode, String email) {
		StockMonitor monitor = stockMonitorRepository.findByStockCodeAndEmail(stockCode, email);
		if (monitor != null) {
			stockMonitorRepository.deleteById(monitor.getId());
		}
	}

	@Override
	public List<StockMonitor> getStockMonitorByEmail(String email) {
		return stockMonitorRepository.findAllByEmail(email);
	}

	@Override
	public StockMonitor createStockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold) throws ClientProtocolException, IOException {
		String stockString = httpService.doGet("http://hq.sinajs.cn/list=" + stockCode);
		// System.out.println(stockInfo);
		stockString = stockString.split("=")[1].replaceAll("\"", "").replace(";", "").trim();
		if (StringUtils.isEmpty(stockString)) {
			return null;
		}
		String[] stockInfo = stockString.split(",");
		StockMonitor monitor = getStockMonitor(stockCode, email, highThreshold, lowThreshold);
		monitor.setCurrentPrice(Double.parseDouble(stockInfo[3]));
		monitor.setStockName(stockInfo[0]);
		monitor.setLastQueryTime(Calendar.getInstance());
		monitor.setHighThreshold(highThreshold);
		monitor.setLowThreshold(lowThreshold);
		stockMonitorRepository.saveAndFlush(monitor);
		return monitor;
	}

}
