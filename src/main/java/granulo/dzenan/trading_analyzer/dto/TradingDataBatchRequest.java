package granulo.dzenan.trading_analyzer.dto;

import java.util.List;

public record TradingDataBatchRequest(String symbol, List<Double> values) { }
