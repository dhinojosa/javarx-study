package com.evolutionnext.javarx;

import org.junit.Test;
import rx.util.async.Async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Daniel Hinojosa
 * @since 1/18/16 6:33 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class ObservableOrderingTest {

    @Test
    public void testObservableOrder() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future fourSecondFuture = executorService.submit(() -> {
            Thread.sleep(4000);
            return 41;
        });
        Future twoSecondFuture = executorService.submit(() -> {
            Thread.sleep(2000);
            return 50;
        });
        Future immediateFuture = executorService.submit(() -> {
            return 50;
        });
    }
}
