package com.evolutionnext.javarx;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Demo 3: Standard Functional Operators
 */
public class ErrorTest {

    @Test
    public void testOnErrorReturnItem() throws InterruptedException {
        Observable<Integer> observable = Observable.fromArray(1, 4, 5, 10, 11, 0, 9, 2);
        Observable<Integer> observable1 = observable.map(x -> 20 / x).onErrorReturnItem(-1);
        observable1.subscribe(System.out::println);
    }

    @Test
    public void testOnErrorResumeNext() throws InterruptedException {
        Observable<Integer> observable = Observable.fromArray(1, 4, 5, 10, 11, 0, 9, 2);
        Observable<Integer> observable1 = observable.map(x -> 20 / x)
                                                    .onErrorResumeNext(Observable.fromArray(3, 6));
        observable1.subscribe(System.out::println);
    }

    @Test
    public void testOnErrorReturn() throws InterruptedException {
        Observable<Integer> observable = Observable.fromArray(1, 4, 5, 10, 11, 0, 9, 2);
        Observable<Integer> observable1 = observable.map(x -> 20 / x).onErrorReturn(throwable -> {
            if (throwable instanceof ArithmeticException) {
                return -1;
            } else {
                return -5;
            }
        });
        observable1.subscribe(System.out::println);
    }
}
