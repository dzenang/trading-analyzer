package granulo.dzenan.tradinganalyzer.dto;

import java.util.List;

public record TradingDataBatchRequest(String symbol, List<Double> values) { }
