package com.hungerfool.stockwatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hungerfool.stockwatcher.http.IdleConnectionEvictor;

@SpringBootApplication
public class StockwatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockwatcherApplication.class, args);
	}
}
