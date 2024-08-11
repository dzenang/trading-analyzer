package granulo.dzenan.trading_analyzer.dto;

public class TradingStatsResponse {
  private double min;
  private double max;
  private double last;
  private double avg;
  private double var;

  // Constructor, getters, and setters

  public TradingStatsResponse(double min, double max, double last, double avg, double var) {
    this.min = min;
    this.max = max;
    this.last = last;
    this.avg = avg;
    this.var = var;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public double getLast() {
    return last;
  }

  public double getAvg() {
    return avg;
  }

  public double getVar() {
    return var;
  }
}
