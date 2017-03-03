package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;


public class HotVsColdObservableTest {

    @Test
    public void testColdObservable() throws InterruptedException {
        Observable<Long> observable = Observable.interval
                (1, TimeUnit.SECONDS).map(x -> x + 1);
        observable
                .subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(1000);
        observable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }

    @Test
    public void testHotObservable() throws InterruptedException {
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1);
        Observable<Long> longObservable = observable
                .publish().autoConnect();
        Thread.sleep(4000);
        longObservable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(3000);
        longObservable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }
}
