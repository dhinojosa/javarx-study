package com.evolutionnext.javarx;


import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class ConditionalObservableTest {

    @Test
    public void testAll() {
        Observable.range(1, 100).all(i -> i % 2 == 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Observable.range(1, 100).all(i -> i > 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }


    @Test
    public void testDefaultIfEmpty() throws InterruptedException {
        Observable<String> observable = Observable.<String>empty()
                .defaultIfEmpty("None");
        observable.subscribe(System.out::println);
        Thread.sleep(3000);
    }
}
