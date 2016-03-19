package com.evolutionnext.javarx;

import org.assertj.core.util.Lists;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.FuncN;
import rx.util.async.Async;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ObservableAdvancedCreationTest {

    @Test
    public void testJust() throws InterruptedException {
        Observable<Integer> observable = Observable.just(10);
        observable.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testRange() throws InterruptedException {
        Observable<Integer> observable = Observable.range(1, 20);
        observable.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testInterval() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).map(x -> {
            System.out.println("Thread Observable: " + Thread.currentThread().getName());
            return x + 1;
        });
        Thread.sleep(1000);
        observable.subscribe(x -> System.out.println("Observable 1: " + x));
        observable.subscribe(x -> System.out.println("Observable 2: " + x));
        Thread.sleep(10000);
    }

    @Test
    public void testStart() throws InterruptedException {
        //Note: The function is called immediately and once, not whenever an
        //observer subscribes to the resulting Observable.
        //Multiple subscriptions to this Observable
        //observe the same return value
        Observable<Integer> observable = Async.start(() -> {
            System.out.format("Running Function in Thread %s\n",
                    Thread.currentThread().getName());
            return 50;
        });
        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Now we are subscribing");
        observable.subscribe(System.out::println);
        observable.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testToAsyncWithAFunc0() throws InterruptedException {
        Func0<Observable<Integer>> observableFunction = Async.toAsync(() -> {
            System.out.format("Running Function in Thread %s\n",
                    Thread.currentThread().getId());
            return 50;
        });
        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Creating the Observables");
        Observable<Integer> observable1 = observableFunction.call();
        Observable<Integer> observable2 = observableFunction.call();
        System.out.println("Now we are subscribing");
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testToAsyncWithAFunc2() throws InterruptedException {
        Func2<Integer, Integer, Observable<Integer>> observableFunction =
                Async.toAsync(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i, Integer j) {
                        System.out.format("Running Function in Thread %s\n",
                                Thread.currentThread().getId());
                        return i + j;
                    }
                });
        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Creating the Observables");
        Observable<Integer> observable1 = observableFunction.call(10, 19);
        Observable<Integer> observable2 = observableFunction.call(40, 90);
        System.out.println("Now we are subscribing");
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testToAsyncWithAFunc3Shortened() throws InterruptedException {
        //Note that the function or Action will only be executed once, even if
        //more than one observer subscribes to the resulting Observable.

        Func3<Integer, Integer, Integer, Observable<Integer>> observableFunction =
                Async.toAsync((i, j, k) -> {
                    System.out.format("Running Function in Thread %s\n",
                            Thread.currentThread().getId());
                    return i + j * k;
                });
        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Creating the Observables");
        Observable<Integer> observable1 = observableFunction.call(10, 19, 90);
        Observable<Integer> observable2 = observableFunction.call(40, 90, 100);
        System.out.println("Now we are subscribing");
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testAsyncAction() throws InterruptedException {
        //Note that the function or Action will only be executed once,
        //even if more than one observer subscribes to the
        //resulting Observable.
        FuncN<Observable<Void>> observableFunction =
                Async.asyncAction
                        (args -> System.out.println(Arrays.toString(args)));
        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Creating the Observables");
        Observable<Void> observable1 = observableFunction.call(10, 19, 90);
        Observable<Void> observable2 = observableFunction.call(40, 90, 100);
        System.out.println("Now we are subscribing");
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testAsyncFunction() throws InterruptedException {
        //Note that the function or Action will only be executed once, even if
        //more than one observer subscribes to the resulting Observable.

        // The following section of code created a stream has already been
        // operated upon or closed

        // FuncN<Observable<Integer>> observableFunction =
        //   Async.asyncFunc(args -> {
        //       System.out.format("Running Function in Thread %s\n",
        //               Thread.currentThread().getId());
        //       Stream<Object> stream = Arrays.asList(args).stream(); //This is too big
        //       boolean allInts =
        //               stream.allMatch(x -> x instanceof Integer);
        //       if (!allInts) return -1;
        //       return stream.mapToInt(value -> (Integer) value).sum();
        //   });

        // "You might also look into the RxJava library, as its processing model
        // lends itself better to this kind of "stream forking"." -- Brian Goetz!

        FuncN<Observable<Integer>> observableFunction =
                Async.asyncFunc(args -> {
                    System.out.format("Running Function in Thread %s\n",
                            Thread.currentThread().getId());
                    return args.length;
                });

        System.out.println("Observable Created");
        Thread.sleep(2000);
        System.out.println("Creating the Observables");
        Observable<Integer> observable1 = observableFunction.call(10, 19, 90);
        Observable<Integer> observable2 = observableFunction
                .call(40, 90, 100, 99);
        System.out.println("Now we are subscribing");
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        observable1.subscribe(System.out::println);
        observable2.subscribe(System.out::println);
        Thread.sleep(2000);
    }


    @Test
    public void createObservableFromFuture() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Observable<Integer> integerObservable = Observable.from(executorService.submit(() -> {
            System.out.println("In Observable:" + Thread.currentThread().getName());
            Thread.sleep(4000);
            return 40 + 100;
        }));

        integerObservable.subscribe(x -> {
            System.out.println("In Subscription: " + Thread.currentThread().getName());
            System.out.println("Recieved:" + x);
        });
    }

    @Test
    public void createObservableFromIterable() {
        List<Integer> numberOfItems = Lists
                .newArrayList(3,5,3,1,4,5,6,8,4,5,2,1,5,3,2);
        Observable<Integer> integerObservable = Observable.from(numberOfItems);

        integerObservable.subscribe(x -> {
            System.out.println("In Subscription: " + Thread.currentThread().getName());
            System.out.println("Received:" + x);
        });
    }

    @Test
    public void createObservableFromArrays() {
        Integer[] numberOfItems =
                new Integer[]{4, 3, 1, 4, 5, 3, 1, 10, 40, 110, 44};
        Observable<Integer> integerObservable = Observable.from(numberOfItems);
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
        Observable<Integer> empty = Observable
                .error(new IllegalArgumentException("This just ain't right"));
        empty.subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(2000);
    }
}