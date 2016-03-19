package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Hinojosa
 * @since 2/23/16 4:51 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class HotVsColdObservableTest {

    @Test
    public void testColdObservable() throws InterruptedException {
        Observable<Long> observable = Observable.interval
                (1, TimeUnit.SECONDS).map(x -> x + 1);
        observable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(1000);
        observable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }

    @Test
    public void testHotObservable() throws InterruptedException {
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1)
                .publish()
                .autoConnect();
        observable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(3000);
        observable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }
}
