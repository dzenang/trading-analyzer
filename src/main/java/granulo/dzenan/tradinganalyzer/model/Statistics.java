package granulo.dzenan.tradinganalyzer.model;

import java.util.ArrayDeque;
import java.util.Deque;

class Statistics {
  private final Deque<Double> prices;
  private final int maxSize;
  private double sum;
  private double sumOfSquares;
  private double min;
  private double max;

  public Statistics(int size) {
    this.prices = new ArrayDeque<>(size);
    this.maxSize = size;
    this.sum = 0.0;
    this.sumOfSquares = 0.0;
    this.min = Double.MAX_VALUE;
    this.max = Double.MIN_VALUE;
  }

  public synchronized void addPrice(double price) {
    if (prices.size() == maxSize) {
      double removedPrice = prices.removeFirst();
      sum -= removedPrice;
      sumOfSquares -= removedPrice * removedPrice;
      // todo recalculate min and max when removing price (probably need to use second min, and second max)
    }
    prices.addLast(price);
    sum += price;
    sumOfSquares += price * price;
    min = Math.min(min, price);
    max = Math.max(max, price);
  }

  public int getSize() {
    return prices.size();
  }

  public int getMaxSize() {
    return maxSize;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public double getLast() {
    return prices.getLast();
  }

  public double getAverage() {
    return sum / prices.size();
  }

  public double getVariance() {
    double mean = getAverage();
    return (sumOfSquares / prices.size()) - (mean * mean);
  }
}
