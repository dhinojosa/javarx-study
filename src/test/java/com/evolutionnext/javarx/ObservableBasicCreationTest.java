package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.observables.StringObservable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.concurrent.*;

public class ObservableBasicCreationTest {

    //Demo 1: Creation of Observables

    @Test
    public void testManualObservableWithManualObserver() throws InterruptedException {
        Observable<Integer> observable = Observable
                .create(new OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        System.out.println
                                ("1:" + Thread.currentThread().getName());
                        System.out.println("Starting the call");
                        subscriber.onNext(40);
                        subscriber.onNext(45);
                        subscriber.onCompleted();
                    }
                });

        System.out.println("Observable Created");
        System.out.println("2:" + Thread.currentThread().getName());

        //Lots of code

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.println();
            }

            @Override
            public void onNext(Integer x) {
                System.out.println("On Next:" +
                        Thread.currentThread().getName());
                System.out.printf("On Next: %d\n", x);
            }
        });

        Thread.sleep(10000);
    }

    @Test
    public void testManualObservableWithManualObserverSimplified() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onCompleted();
                }
        );
        a.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.println();
            }

            @Override
            public void onNext(Integer x) {
                System.out.printf("On Next: %d\n", x);
            }
        });
    }


    @Test
    public void testManualObservableWithAction1() {
        Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onCompleted();
                }
        ).subscribe(System.out::println);
    }


    @Test
    public void testManualObservableWithAction1ForSuccessAndAction1ForException() {
       Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onCompleted();
                }
        ).subscribe(System.out::println,
               Throwable::printStackTrace,
                () -> System.out.println("We are done!"));
    }

    @Test
    public void testManualObservableWithExplicitActions() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onCompleted();
                }
        );
        a.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }


    @Test
    public void testManualObservableWithLambdaActions() {
        Observable<Integer> a = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(40);
                subscriber.onNext(45);
                subscriber.onCompleted();
            }
        });

        a.subscribe(
                integer -> {
                    System.out.println("Received: " + integer);
                    throw new IllegalArgumentException("Oh no");
                },
                e -> {
                    e.printStackTrace();
                    System.out.println("Got it! Nabbed it");
                },
                () -> System.out.println("Completed"));

        a.subscribe(System.out::println,
                e -> {
                    System.out.println("Inside of 2nd Sub");
                    e.printStackTrace();
                },
                () -> System.out.println("Completed"));
    }

    @Test
    public void testManualObservableWithManualObserverSimplifiedFurther() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onCompleted();
                }
        );
        a.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }


    @Test
    public void testBasicObservableCompletelySimplified() throws InterruptedException {
        Observable.just(40, 45)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Completed"));

        Thread.sleep(2000);
    }

    @Test
    public void testMap() {
        Observable<Integer> integerObservable =
                Observable.just(1, 2, 3);

        Observable<String> stringObservable =
                integerObservable
                .map(integer -> "Hello: " + integer);

        stringObservable
                .subscribe(System.out::println);
    }

    @Test
    public void testBasicFlatMap() throws InterruptedException {
        Observable<Integer> a =
                Observable.just(50, 100, 122);
        Observable<Integer> b =
                a.flatMap(x -> Observable.just(x - 1, x, x + 1));

        b.subscribe(System.out::println);
        System.out.println("-----------");
        Thread.sleep(2000);
        b.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testFromWithFuture() throws InterruptedException {
        ExecutorService executorService =
                Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(
                new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        System.out.println
                                ("Thread name in future" +
                                Thread.currentThread().getName());
                        Thread.sleep(1000);
                        return 19;
                    }
                });

        Observable.from(future).map(x -> x + 30)
                .repeat(5)
                .subscribe(System.out::println);

        Thread.sleep(15000);
    }

    @Test
    public void testInterval() throws InterruptedException {
        Observable<String> interval =
                Observable.interval(1, TimeUnit.SECONDS).map(Long::toHexString);

        interval.subscribe(lng ->
                System.out.println("1: lng = " + lng));

        Thread.sleep(5000);
        interval.subscribe(lng ->
                System.out.println("2: lng = " + lng));

        Thread.sleep(10000);
    }

    /**
     * Defer will delay any emission of items until an Observer subscribes
     *
     * @throws InterruptedException
     */
    @Test
    public void testDefer() throws InterruptedException {
        Observable<LocalTime> localTimeObservable =
                Observable.defer(
                        () -> Observable
                                .just(LocalTime.now()))
                                   .repeat(3);
        localTimeObservable.subscribe(System.out::println);
        Thread.sleep(3000);
        System.out.println("Next Subscriber");
        localTimeObservable.subscribe(System.out::println);
    }


    @Test
    public void testRange() throws InterruptedException {
        Observable<Integer> rangeObservable =
                Observable.range(10, 20);

        rangeObservable.subscribe(System.out::println);

        Thread.sleep(3000);

        System.out.println("-------------");
        System.out.println("Next Subscriber");
        System.out.println("-------------");

        rangeObservable
                .map(x -> x * 4)
                .repeat(5)
                .subscribe(System.out::println);
    }


    @Test
    public void testTicker() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> stockObservable =
                Observable.from(ticker);
        TickerPriceFinder tickerPriceFinder =
                TickerPriceFinder.create();
        stockObservable
                .flatMap(s ->
                        Observable.from
                                (tickerPriceFinder.getPrice(s)))
                .subscribe(System.out::println);
    }

    @Test
    public void testTickerToList() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> originalObservable =
                Observable.from(ticker);
        TickerPriceFinder tickerPriceFinder =
                TickerPriceFinder.create();
        StringObservable.join(
                originalObservable.flatMap(s ->
                        Observable.from(
                                tickerPriceFinder.getPrice(s))
                ).map(d -> String.format("%.2f", d)), ",")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testTweets() {
        String[] tweets = new String[]{
                "Learn how the cloud can help with your #Agile development process & DevOps activities #Java",
                "It's alive! #ScalaExercises V.2. Free community tool for learning #Scala. #OpenSource",
                "For any #clojure nerds playing with #ethereum my library Cloth now also creates an API for your Smart Contract"};


        Observable<HashTag> hashTags = Observable
                .from(tweets)
                .flatMap(x -> Observable.from(x.split(" ")))
                .filter(x -> x.startsWith("#"))
                .map((tag) -> new HashTag(tag));


        hashTags
                .collect(HashSet::new,
                        HashSet::add)
                .subscribe(System.out::println);

//        //Another Branch
        Observable<List<HashTag>> listObservable = hashTags.toSortedList();

        listObservable.subscribe(new Observer<List<HashTag>>() {
            @Override
            public void onCompleted() {
                System.out.printf("Done");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<HashTag> hashTags) {
                System.out.println("List = " + hashTags);
            }
        });
    }
}
