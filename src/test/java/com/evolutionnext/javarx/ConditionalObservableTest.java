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
    public void testAmb() throws InterruptedException {
        Observable<Integer> oneTo10 = Observable.range(1, 5)
                .delay(5, TimeUnit.SECONDS);
        Observable<Integer> tenTo20 = Observable.range(10, 10)
                .delay(2, TimeUnit.SECONDS);
        Observable<Integer> twentyTo30 = Observable.range(30, 10)
                .delay(8, TimeUnit.SECONDS);

        Observable.amb(oneTo10, tenTo20, twentyTo30)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(10000);
    }


    @Test
    public void testAmbWith() throws InterruptedException {
        Observable<Integer> oneTo10 = Observable.range(1, 10)
                .delay(5, TimeUnit.SECONDS);
        Observable<Integer> tenTo20 = Observable.range(30, 10)
                .delay(2, TimeUnit.SECONDS);
        Observable<Integer> twentyTo30 = Observable.range(90, 10)
                .delay(8, TimeUnit.SECONDS);

        oneTo10.ambWith(tenTo20).ambWith(twentyTo30)
                .subscribe(System.out::println, Throwable::printStackTrace);

        Thread.sleep(10000);
    }

    @Test
    public void testDefaultIfEmpty() throws InterruptedException {
        Observable<String> observable = Observable.<String>empty()
                .defaultIfEmpty("None");
        observable.subscribe(System.out::println);
        Thread.sleep(3000);
    }
}
