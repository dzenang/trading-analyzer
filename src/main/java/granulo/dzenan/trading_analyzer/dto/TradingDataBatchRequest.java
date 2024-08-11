package granulo.dzenan.trading_analyzer.dto;

import java.util.List;

public class TradingDataBatchRequest {
  private String symbol;
  private List<Double> values;

  // Getters and setters

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public List<Double> getValues() {
    return values;
  }

  public void setValues(List<Double> values) {
    this.values = values;
  }
}
