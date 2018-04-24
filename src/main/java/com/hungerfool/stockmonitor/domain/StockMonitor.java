package com.hungerfool.stockmonitor.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stockmonitor")
public class StockMonitor extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private double currentPrice;
	private String email;
	private Double highThreshold;
	private Calendar lastQueryTime;
	private Double lowThreshold;
	private String stockCode;
	private String stockName;

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public StockMonitor() {
		super();
	}

	public StockMonitor(String stockCode, String email, Double highThreshold, Double lowThreshold) {
		super();
		this.stockCode = stockCode;
		this.email = email;
		this.lowThreshold = lowThreshold;
		this.highThreshold = highThreshold;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof StockMonitor)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		StockMonitor other = (StockMonitor) obj;
		return getStockCode().equals(other.getStockCode()) && getEmail().equals(other.getEmail());
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public String getEmail() {
		return email;
	}

	public Double getHighThreshold() {
		return highThreshold;
	}

	public Calendar getLastQueryTime() {
		return lastQueryTime;
	}

	public Double getLowThreshold() {
		return lowThreshold;
	}

	public String getStockCode() {
		return stockCode;
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getStockCode() ? 0 : getStockCode().hashCode() * 31;
		hashCode += null == getEmail() ? 0 : getEmail().hashCode() * 31;
		return hashCode;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setHighThreshold(Double highThreshold) {
		this.highThreshold = highThreshold;
	}

	public void setLastQueryTime(Calendar lastQueryTime) {
		this.lastQueryTime = lastQueryTime;
	}

	public void setLowThreshold(Double lowThreshold) {
		this.lowThreshold = lowThreshold;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

}
