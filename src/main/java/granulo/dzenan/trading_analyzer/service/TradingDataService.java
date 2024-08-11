package granulo.dzenan.trading_analyzer.service;

import granulo.dzenan.trading_analyzer.dto.TradingStatsResponse;
import granulo.dzenan.trading_analyzer.model.TradingData;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TradingDataService {
  private final Map<String, TradingData> dataStore = new ConcurrentHashMap<>(10);

  public String addBatchTradingData(String symbol, List<Double> values) {
    dataStore.computeIfAbsent(symbol, k -> new TradingData())
        .addBatch(values);
    return "Batch data added for symbol: " + symbol;
  }

  public TradingStatsResponse getStatsForWindow(String symbol, int windowSize) {
    TradingData tradingData = dataStore.get(symbol);
    if (tradingData == null) {
      throw new IllegalArgumentException("Symbol not found: " + symbol);
    }
    int numberOfPoints = (int) Math.pow(10, windowSize);
    return tradingData.calculateStats(numberOfPoints);
  }
}
