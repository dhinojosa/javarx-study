package com.evolutionnext.javarx;


import org.junit.Test;
import rx.Observable;

public class ObservableLazinessTest {

    @Test
    public void testLaziness() throws InterruptedException {
        Observable<Integer> observable = Observable.fromCallable(() -> {
            System.out.println("Processing Observable");
            return 100;
        });
        System.out.println("Sleeping");
        Thread.sleep(2000);
        observable.subscribe(System.out::println);
        observable.subscribe(System.out::println);
    }
}
