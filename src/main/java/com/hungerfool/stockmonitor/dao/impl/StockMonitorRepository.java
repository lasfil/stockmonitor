package com.hungerfool.stockmonitor.dao.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hungerfool.stockmonitor.domain.StockMonitor;
@Repository
public interface StockMonitorRepository extends JpaRepository<StockMonitor, Long>{
	public StockMonitor findByStockCodeAndEmail(String stockCode, String email);
	
	public List<StockMonitor> findAllByEmail(String email);

}
