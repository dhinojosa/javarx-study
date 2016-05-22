package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.GroupedObservable;
import rx.observables.StringObservable;

import java.time.LocalTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservableBasicCreationTest {

    @Test
    public void testManualObservableWithManualObserver() throws InterruptedException {
        Observable<Integer> a = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("1:" + Thread.currentThread().getName());
                System.out.println("Starting the call");
                subscriber.onNext(40);
                subscriber.onNext(45);
                subscriber.onCompleted();
            }
        });

        System.out.println("Observable Created");
        System.out.println("2:" + Thread.currentThread().getName());


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
                System.out.println("On Next:" + Thread.currentThread().getName());
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
        Observable<Integer> a = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(40);
                subscriber.onNext(45);
                subscriber.onCompleted();
            }
        });
        a.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer x) {
                System.out.println(x);
            }
        });
    }


    @Test
    public void testManualObservableWithAction1ForSuccessAndAction1ForException() {
        Observable<Integer> a = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(40);
                subscriber.onNext(45);
                //subscriber.onError(new IllegalArgumentException("Wow"));
                subscriber.onCompleted();
            }
        });


        a.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("integer = " + integer);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("Am I here?");
                throwable.printStackTrace();
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("We are done!");
            }
        });
    }

    @Test
    public void testManualObservableWithExplicitActions() {
        Observable<Integer> a = Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(40);
                subscriber.onNext(45);
                subscriber.onCompleted();
            }
        });
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
                integer -> System.out.println("Received: " + integer),
                e -> {
                    e.printStackTrace();
                    System.out.println();
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
    public void testBasicFlatMap() throws InterruptedException {
        Observable<Integer> a = Observable.just(50, 100, 122);
        Observable<Integer> b = a.flatMap(x -> Observable.just(x - 1, x, x + 1));
        a.subscribe(System.out::println);
        System.out.println("-----------");
        Thread.sleep(2000);
        b.subscribe(System.out::println);
        Thread.sleep(2000);
    }



    @Test
    public void testBasicAdditionLambda() throws InterruptedException {
        Observable<Integer> a = Observable.create(subscriber ->
                {
                    subscriber.onNext(40);
                    subscriber.onNext(45);
                    subscriber.onCompleted();
                }
        );
        Observable<Integer> b = a.flatMap(x -> Observable.just(x + 1));
        b.subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testFromWithFuture() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Observable<Integer> observable = Observable.from(executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 19;
            }
        }));

        observable.subscribe(x -> System.out.println(x));
        Thread.sleep(5000);

    }

    /**
     * Defer will delay any emission of items until an Observer subscribes
     *
     * @throws InterruptedException
     */
    @Test
    public void testDefer() throws InterruptedException {
        Observable<LocalTime> localTimeObservable =
                Observable.defer(() -> Observable
                        .just(LocalTime.now())).repeat(3);
        localTimeObservable.subscribe(System.out::println);
        Thread.sleep(3000);
        System.out.println("Next Subscriber");
        localTimeObservable.subscribe(System.out::println);
    }

    @Test
    public void testTicker() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> stringObservable = Observable.from(ticker);
        TickerPriceFinder tickerPriceFinder = TickerPriceFinder.create();
        stringObservable
                .flatMap(s -> Observable.from(tickerPriceFinder.getPrice(s)))
                .subscribe(System.out::println);
    }

    @Test
    public void testTickerToList() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> originalObservable = Observable.from(ticker);
        TickerPriceFinder tickerPriceFinder = TickerPriceFinder.create();
        StringObservable.join(
                originalObservable.flatMap(s ->
                        Observable.from(tickerPriceFinder.getPrice(s))
                ).map(d -> String.format("%.2f", d)), ",")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
