package granulo.dzenan.trading_analyzer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TradingAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingAnalyzerApplication.class, args);
	}

}

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
	public TradingStatsResponse getStats(@RequestParam String symbol, @RequestParam int k) {
		TradingData tradingData = dataStore.get(symbol);
		if (tradingData == null) {
			throw new IllegalArgumentException("Symbol not found: " + symbol);
		}
		int numberOfPoints = (int) Math.pow(10, k);
		return tradingData.calculateStats(numberOfPoints);
	}
}

class TradingDataBatchRequest {
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

class TradingData {
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
		}
		prices.addLast(price);
		sum += price;
		sumOfSquares += price * price;
		min = Math.min(min, price);
		max = Math.max(max, price);
	}

	public int getSize() {
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

class TradingStatsResponse {
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
