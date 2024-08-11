package granulo.dzenan.trading_analyzer.controller;

import granulo.dzenan.trading_analyzer.dto.TradingDataBatchRequest;
import granulo.dzenan.trading_analyzer.dto.TradingStatsResponse;
import granulo.dzenan.trading_analyzer.model.TradingData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
class TradingDataController {

  private final Map<String, TradingData> dataStore = new ConcurrentHashMap<>(10);

  @PostMapping("/add_batch")
  public String addBatch(@RequestBody TradingDataBatchRequest request) {
    dataStore.computeIfAbsent(request.getSymbol(), k -> new TradingData())
        .addBatch(request.getValues());
    return "Batch data added for symbol: " + request.getSymbol();
  }

  @GetMapping("/stats")
  public TradingStatsResponse getStats(@RequestParam String symbol, @RequestParam int windowSize) {
    TradingData tradingData = dataStore.get(symbol);
    if (tradingData == null) {
      throw new IllegalArgumentException("Symbol not found: " + symbol);
    }
    int numberOfPoints = (int) Math.pow(10, windowSize);
    return tradingData.calculateStats(numberOfPoints);
  }
}
