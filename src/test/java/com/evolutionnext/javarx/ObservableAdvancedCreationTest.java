package com.evolutionnext.javarx;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ObservableAdvancedCreationTest {

    @Test
    public void createFromCallable() {
        Observable.fromCallable(() -> "Foo").subscribe(x -> System.out.println("x = " + x));
    }

    @Test
    public void createObservableFromFuture() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Observable<Integer> integerObservable = Observable.fromFuture(
                executorService.submit(() -> {
                    System.out.println("In Observable:" + Thread.currentThread().getName());
                    Thread.sleep(4000);
                    return 40 + 100;
                }));

        System.out.println("Now that that's running....");

        integerObservable.subscribeOn(Schedulers.newThread()).subscribe(x -> {
            System.out.println("In Subscription: " + Thread.currentThread().getName());
            System.out.println("Received:" + x);
        });

        Thread.sleep(1000);
        integerObservable.subscribe(x -> {
             System.out.println("Recieved 2: " + x);
        });

        Thread.sleep(10000);
    }

    @Test
    public void createObservableFromIterable() {
        List<Integer> numberOfItems = Lists
                .newArrayList(3,5,3,1,4,5,6,8,4,5,2,1,5,3,2);
        Observable<Integer> integerObservable = Observable.fromIterable(numberOfItems);

        integerObservable.subscribe(x -> {
            System.out.println("In Subscription: " + Thread.currentThread().getName());
            System.out.println("Received:" + x);
        });
    }

    @Test
    public void createObservableFromArrays() {
        Integer[] numberOfItems =
                new Integer[]{4, 3, 1, 4, 5, 3, 1, 10, 40, 110, 44};
        Observable<Integer> integerObservable = Observable.fromArray(numberOfItems);
        integerObservable.subscribe(x -> {
            System.out.println("In Subscription: " + Thread.currentThread().getName());
            System.out.println("Received:" + x);
        });
    }

    @Test
    public void createObservableEmpty() {
        Observable<Integer> empty = Observable.empty();
        empty.subscribe(System.out::println);
    }

    @Test
    public void createObservableNever() throws InterruptedException {
        Observable<Integer> empty = Observable.never();
        empty.subscribe(System.out::println);
        Thread.sleep(100000);
    }

    @Test
    public void createObservableError() throws InterruptedException {
        Observable<Integer> error = Observable
                .error(new IllegalArgumentException("This just ain't right"));
        error.subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(2000);
    }
}











