package com.evolutionnext.javarx;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import org.junit.Test;

public class ConditionalObservableTest {

    @Test
    public void testAllObservable() {
        Observable.range(1, 100).all(i -> i % 2 == 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Observable.range(1, 100).all(i -> i > 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testAllFlowable() {
        Flowable.range(1, 100).all(i -> i % 2 == 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Flowable.range(1, 100).all(i -> i > 0)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testDefaultIfEmptyObservable() throws InterruptedException {
        Observable<String> observable = Observable.<String>empty().defaultIfEmpty("None");
        observable.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void testDefaultIfEmptyFlowable() throws InterruptedException {
        Flowable<String> flowable = Flowable.<String>empty().defaultIfEmpty("None");
        flowable.subscribe(System.out::println);
        Thread.sleep(3000);
    }
}
