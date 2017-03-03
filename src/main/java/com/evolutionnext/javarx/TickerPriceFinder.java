package com.evolutionnext.javarx;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Daniel Hinojosa
 * @since 1/12/16 9:46 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class TickerPriceFinder {

    private final ExecutorService executorService;
    private final Random random;

    public TickerPriceFinder(Random random, ExecutorService executorService) {
        this.random = random;
        this.executorService = executorService;
    }

    public Future<Double> getPrice(String name) {

        return executorService.submit(
                () -> random.nextDouble() * 200.0);
    }

    public static TickerPriceFinder create() {
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        return new TickerPriceFinder(random, executorService);
    }
}