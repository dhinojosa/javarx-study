package com.evolutionnext.javarx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


public class HotVsColdObservableTest {

    @Test
    public void testColdObservable() throws InterruptedException {
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1);

        observable
                .subscribe(x -> System.out.println("Observer 1: " + x));

        Thread.sleep(5000);
        observable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(25000);
    }

    @Test
    public void testColdFlowable() throws InterruptedException {
        Flowable<Long> flowable = Flowable.interval
                (1, TimeUnit.SECONDS).map(x -> x + 1);
        flowable
                .subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(1000);
        flowable.subscribe(x -> System.out.println("Observer 2: " + x));
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

    @Test
    public void testHotObservableWithRefCount() throws InterruptedException {
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1);
        Observable<Long> longObservable = observable
                .publish().refCount();
        Thread.sleep(4000);
        longObservable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(3000);
        longObservable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }

    @Test
    public void testHotObservableShare() throws InterruptedException {
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1);
        Observable<Long> longObservable = observable.share();
        Thread.sleep(4000);
        longObservable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(3000);
        longObservable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(500);


        Thread.sleep(10000);
    }


    @Test
    public void testHotFlowable() throws InterruptedException {
        Flowable<Long> flowable = Flowable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> x + 1);
        Flowable<Long> longObservable = flowable
                .publish().autoConnect();
        Thread.sleep(4000);
        longObservable.subscribe(x -> System.out.println("Observer 1: " + x));
        Thread.sleep(3000);
        longObservable.subscribe(x -> System.out.println("Observer 2: " + x));
        Thread.sleep(10000);
    }
}
