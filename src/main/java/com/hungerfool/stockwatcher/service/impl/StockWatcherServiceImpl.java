package com.hungerfool.stockwatcher.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hungerfool.stockwatcher.dao.impl.StockWatcherRepository;
import com.hungerfool.stockwatcher.domain.StockWatcher;
import com.hungerfool.stockwatcher.email.EmailService;
import com.hungerfool.stockwatcher.http.HttpService;
import com.hungerfool.stockwatcher.service.StockWatcherService;

@Service
public class StockWatcherServiceImpl implements StockWatcherService {
	@Autowired
	StockWatcherRepository stockWatcherRepository;

	@Autowired
	private HttpService httpService;

	@Autowired
	private EmailService emailService;

	SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

	public static String emailTitle = "您设置的股票价格发生变化";

	@Override
	public StockWatcher queryStockPrice(String stockCode, String email) throws ClientProtocolException, IOException {
		String stockString = httpService.doGet("http://hq.sinajs.cn/list=" + stockCode);
		// System.out.println(stockInfo);
		String[] stockInfo = stockString.split("=")[1].replaceAll("\"", "").replace(";", "").split(",");
		StockWatcher watcher = stockWatcherRepository.findByStockCodeAndEmail(stockCode, email);
		watcher.setCurrentPrice(Double.parseDouble(stockInfo[3]));
		watcher.setStockName(stockInfo[0]);
		watcher.setLastQueryTime(Calendar.getInstance());
		stockWatcherRepository.saveAndFlush(watcher);
		return watcher;
	}

	@Override
	public StockWatcher getStockWatcher(String stockCode, String email, Double highThreshold, Double lowThreshold) {
		StockWatcher watcher = stockWatcherRepository.findByStockCodeAndEmail(stockCode, email);
		if (watcher == null) {
			watcher = stockWatcherRepository.save(new StockWatcher(stockCode, email, highThreshold, lowThreshold));
		} else {
			watcher.setHighThreshold(highThreshold);
			watcher.setLowThreshold(lowThreshold);
			stockWatcherRepository.saveAndFlush(watcher);
		}

		return watcher;

	}

	@Override
	public void checkNotification(StockWatcher watcher) {
		if (watcher == null) {
			return;
		}

		if ((watcher.getLowThreshold() != null && watcher.getCurrentPrice() > watcher.getLowThreshold())
				&& (watcher.getHighThreshold() != null && watcher.getCurrentPrice() < watcher.getHighThreshold())) {
			return;
		}
		StringBuilder content = new StringBuilder();
		content.append(sdf.format(watcher.getLastQueryTime().getTime())).append("[").append(watcher.getStockName())
				.append("]").append("目前价格").append(watcher.getCurrentPrice());
		if (watcher.getHighThreshold() != null && watcher.getCurrentPrice() > watcher.getHighThreshold()) {
			content.append("已高于高位提醒价").append(watcher.getHighThreshold());
		}

		if (watcher.getLowThreshold() != null && watcher.getCurrentPrice() < watcher.getLowThreshold()) {
			content.append("已低于低位提醒价").append(watcher.getLowThreshold());
		}

		 System.out.println(content);
		// emailService.sendSimpleMail(watcher.getEmail(), emailTitle,
		// content.toString());

	}

	@Override
	public void deleteWatcher(String stockCode, String email) {
		StockWatcher watcher = stockWatcherRepository.findByStockCodeAndEmail(stockCode, email);
		if (watcher != null) {
			stockWatcherRepository.deleteById(watcher.getId());
		}
	}

}
