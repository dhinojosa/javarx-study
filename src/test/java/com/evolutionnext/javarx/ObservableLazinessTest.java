package com.evolutionnext.javarx;


import org.junit.Test;
import rx.Observable;

public class ObservableLazinessTest {

    @Test
    public void testLaziness() throws InterruptedException {

        //Hot
        Observable<Integer> observable1 = Observable.create(subscriber -> {
            System.out.printf("Starting Observable1 in Thread %s\n", Thread.currentThread().getId());
            subscriber.onNext(10);
            System.out.printf("Ending Observable1 in Thread %s\n", Thread.currentThread().getId());
        });

        //Cold
        Observable<Integer> observable2 = Observable.fromCallable(() -> {
            System.out.printf("Starting Observable2 in Thread %s\n", Thread.currentThread().getId());
            int result = 100;
            System.out.printf("Ending Observable2 in Thread %s\n", Thread.currentThread().getId());
            return result;
        });


        System.out.println("Sleeping");
        Thread.sleep(2000);
        observable1.subscribe(System.out::println);
        observable1.subscribe(System.out::println);

        Thread.sleep(2000);
        observable2.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
    }
}
