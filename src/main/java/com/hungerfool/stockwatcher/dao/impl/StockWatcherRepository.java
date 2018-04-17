package com.hungerfool.stockwatcher.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hungerfool.stockwatcher.domain.StockWatcher;
@Repository
public interface StockWatcherRepository extends JpaRepository<StockWatcher, Long>{
	public StockWatcher findByStockCodeAndEmail(String stockCode, String email);

}
