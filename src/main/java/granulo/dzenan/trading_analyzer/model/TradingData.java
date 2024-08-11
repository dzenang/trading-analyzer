package granulo.dzenan.trading_analyzer.model;

import granulo.dzenan.trading_analyzer.dto.TradingStatsResponse;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TradingData {
  private final List<Statistics> statisticsList;

  public TradingData() {
    statisticsList = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      statisticsList.add(new Statistics((int) Math.pow(10, i + 1)));
    }
  }

  public synchronized void addBatch(List<Double> prices) {
    for (double price : prices) {
      addPrice(price);
    }
  }

  public synchronized void addPrice(double price) {
    for (Statistics stats : statisticsList) {
      stats.addPrice(price);
    }
  }

  public synchronized TradingStatsResponse calculateStats(int numberOfPoints) {
    for (Statistics stats : statisticsList) {
      if (stats.getSize() == numberOfPoints) {
        return new TradingStatsResponse(
            stats.getMin(),
            stats.getMax(),
            stats.getLast(),
            stats.getAverage(),
            stats.getVariance()
        );
      }
    }
    throw new IllegalArgumentException("No statistics available for " + numberOfPoints + " data points.");
  }
}
