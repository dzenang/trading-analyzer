package granulo.dzenan.trading_analyzer.controller;

import granulo.dzenan.trading_analyzer.dto.TradingDataBatchRequest;
import granulo.dzenan.trading_analyzer.dto.TradingStatsResponse;
import granulo.dzenan.trading_analyzer.service.TradingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
class TradingDataController {

  private final TradingDataService tradingDataService;

  @Autowired
  public TradingDataController(TradingDataService tradingDataService) {
    this.tradingDataService = tradingDataService;
  }

  @PostMapping("/add_batch")
  public String addBatch(@RequestBody TradingDataBatchRequest request) {
    return tradingDataService.addBatchTradingData(request.symbol(), request.values());
  }

  @GetMapping("/stats")
  public TradingStatsResponse getStats(@RequestParam String symbol, @RequestParam int windowSize) {
    return tradingDataService.getStatsForWindow(symbol, windowSize);
  }
}
