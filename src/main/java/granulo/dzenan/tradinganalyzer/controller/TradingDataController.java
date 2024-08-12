package granulo.dzenan.tradinganalyzer.controller;

import granulo.dzenan.tradinganalyzer.dto.TradingDataBatchRequest;
import granulo.dzenan.tradinganalyzer.exception.StatsNotFoundException;
import granulo.dzenan.tradinganalyzer.service.TradingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to work with trading data
 */
@RestController
@RequestMapping("/api/v1")
class TradingDataController {

  private final TradingDataService tradingDataService;

  @Autowired
  public TradingDataController(TradingDataService tradingDataService) {
    this.tradingDataService = tradingDataService;
  }

  /**
   * Method to add trading data as batch
   * @param request data points for specific symbol to be added to memory
   * @return 200 when data is added to memory
   */
  @PostMapping("/add_batch")
  public ResponseEntity<String> addBatch(@RequestBody TradingDataBatchRequest request) {
    return new ResponseEntity<>(tradingDataService.addBatchTradingData(request.symbol(), request.values()),
        HttpStatus.OK);
    //todo should handle negative scenario, network failure etc.
  }

  /**
   * Method to get statistics for specific symbol and window size
   * @param symbol string representing type of data stored in memory
   * @param windowSize size of data to analyze in as power of 10,
   *                   possible values 1-8
   * @return 200 requested stats are available
   *         404 stats not found
   */
  @GetMapping("/stats")
  public ResponseEntity<Object> getStats(@RequestParam String symbol, @RequestParam int windowSize) {
    try {
      return new ResponseEntity<>(tradingDataService.getStatsForWindow(symbol, windowSize),
          HttpStatus.OK);
    } catch (StatsNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
