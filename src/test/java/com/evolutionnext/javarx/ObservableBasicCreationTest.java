package com.evolutionnext.javarx;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ObservableBasicCreationTest {

    //Demo 1: Create a Flowable
    @Test
    public void testFlowable() {
        Flowable<Integer> flowable = Flowable.create
                (new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer>
                                           flowableEmitter)
                                           throws Exception {
                System.out.println
                        ("1:" + Thread.currentThread().getName());
                System.out.println("Starting the call");
                flowableEmitter.onNext(40);
                flowableEmitter.onNext(45);
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        System.out.println("Flowable Created");
        System.out.println("2:" + Thread.currentThread().getName());

        //Few hundred.

        flowable.subscribe(
                new org.reactivestreams.Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(5);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("On Next:" +
                        Thread.currentThread().getName());
                System.out.printf("On Next: %d\n", integer);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Flowable Completed");
            }
        });
    }

    @Test
    public void testManualObservableWithManualObserverSimplified() {
        Observable<Integer> a = Observable.create(
                s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onComplete();
                }
        );

        a.subscribe(new Observer<Integer>() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                System.out.println();
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }

            @Override
            public void onSubscribe(Disposable d) {

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
                    s.onComplete();
                }
        ).subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }


    @Test
    public void testManualObservableWithAction1ForSuccessAndAction1ForException() {
        Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onComplete();
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
                    s.onComplete();
                }
        );
        a.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("Completed"));
    }


    @Test
    public void testManualObservableWithLambdaActions() {
        Observable<Integer> a = Observable.create(e -> {
            System.out.println("In Observale:" + Thread.currentThread().getName());
            e.onNext(40);
            e.onNext(45);
            e.onComplete();
        });

        a.subscribe(
                integer -> {
                    System.out.println("In Sub1:" + Thread.currentThread().getName());
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
                    System.out.println("In Sub2:" + Thread.currentThread().getName());
                    System.out.println("Exception Inside of 2nd Sub");
                    e.printStackTrace();
                },
                () -> System.out.println("Completed"));
    }

    @Test
    public void testManualObservableWithManualObserverSimplifiedFurther() {
        Observable<Integer> a = Observable.create(s -> {
                    s.onNext(40);
                    s.onNext(45);
                    s.onComplete();
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
    public void testBasicFlowableCompletelySimplified() throws InterruptedException {
        Flowable.just(40, 45)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Completed"));

        Thread.sleep(2000);
    }


    @Test
    public void testBasicSingleCompletelySimplified() throws InterruptedException {
        Single.just(40).subscribe(System.out::println,
                Throwable::printStackTrace); //No completed
        Thread.sleep(2000);
    }

    @Test
    public void testBasicMaybeCompletelySimplified() throws InterruptedException {
        Maybe.<Integer>empty().subscribe(new MaybeObserver<Integer>() {
                                             @Override
                                             public void onSubscribe(Disposable d) {

                                             }

                                             @Override
                                             public void onSuccess(Integer integer) {

                                             }

                                             @Override
                                             public void onError(Throwable e) {

                                             }

                                             @Override
                                             public void onComplete() {

                                             }
                                         });
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
                a.flatMap(x ->
                        Observable.just(x - 1, x, x + 1));
        b.subscribe(System.out::println);
        System.out.println("-----------");
        Thread.sleep(2000);
        b.map(x -> "Hello:" + x).subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testFromWithFuture() throws InterruptedException {
        ExecutorService executorService =
                Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(
                () -> {
                    System.out.println
                            ("Thread name in future" +
                                    Thread.currentThread().getName());
                    Thread.sleep(1000);
                    return 19;
                });

        Observable<Integer> observable = Observable.fromFuture(future);

        observable.map(x -> x + 30)
                  .repeat(5)
                  .subscribe(System.out::println);

        observable.flatMap(x -> Observable.just(x + 40, x + 50))
                  .subscribe(System.out::println);

        Thread.sleep(15000);
    }

    @Test
    public void testInterval() throws InterruptedException {
        Observable<String> interval =
                Observable.interval
                        (1, TimeUnit.SECONDS)
                          .map(Long::toHexString);

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
                Observable.fromArray(ticker);
        TickerPriceFinder tickerPriceFinder =
                TickerPriceFinder.create();
        stockObservable
                .flatMap(s ->
                        Observable.fromFuture
                                (tickerPriceFinder.getPrice(s)))

                .subscribe(System.out::println);
    }

    @Test
    public void testTweets() {
        String[] tweets = new String[]{
                "Learn how the cloud can help with your #Agile development process & DevOps activities #Java",
                "It's alive! #ScalaExercises V.2. Free community tool for learning #Scala. #OpenSource",
                "For any #clojure nerds playing with #ethereum my library Cloth now also creates an API for your Smart Contract"};

        Observable<HashTag> hashTags = Observable
                .fromArray(tweets)
                .flatMap(x -> Observable.fromArray(x.split(" ")))
                .filter(x -> x.startsWith("#"))
                .map(HashTag::new);


        hashTags
                .collect(ArrayList::new,
                        ArrayList::add)
                .subscribe(System.out::println);

//        //Another Branch
        Single<List<HashTag>> listObservable = hashTags.toSortedList();

        listObservable.subscribe(new SingleObserver<List<HashTag>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<HashTag> hashTags) {

                System.out.println(hashTags);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }
}
