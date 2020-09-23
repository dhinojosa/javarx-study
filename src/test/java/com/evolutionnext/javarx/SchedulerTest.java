package com.evolutionnext.javarx;


import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.rxjava3.core.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {

    private Observable<Integer> source;


    /**
     * Setting up a basic Observable for the two tests
     */
    @Before
    public void setUp() {
        source = Observable.create(
                o -> {
                    System.out.format(
                            "Source begins: invoked on threadName:%s\n",
                            Thread.currentThread().getName());
                    o.onNext(1);
                    o.onNext(2);
                    o.onNext(3);
                    o.onComplete();
                    System.out.format(
                            "Finished on threadName:%s\n",
                            Thread.currentThread().getName());
                }
        );
    }

    @Test
    public void testProofThatAllIsSingleThreaded() throws InterruptedException {
        //RX is single threaded
        //Translated from: http://www.introtorx.com/Content/v1.0.10621.0/15_SchedulingAndThreading.html
        System.out.format("Starting on threadName:%s\n",
                Thread.currentThread().getName());

        Disposable disposable = source.subscribe(i ->
                System.out.format("Received %d on threadName:%s\n", i,
                    Thread.currentThread().getName()),
            Throwable::printStackTrace,
            () -> System.out.format("Completed on threadName:%s\n",
                Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n",
                Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    @Test
    public void testMultithreadedWithSubscribeOnScheduler() throws InterruptedException {
        System.out.format("Starting on threadName:%s\n", Thread.currentThread().getName());
        Disposable subscribe = source.doOnNext(x ->
            System.out.format("Source on thread: %s\n",
                Thread.currentThread().getName()))
                                     .subscribeOn(Schedulers.newThread())
                                     .map(x -> x + 1)
                                     .doOnNext(x ->
                                         System.out.format("Map on thread: " +
                                                 "%s\n",
                                             Thread.currentThread().getName()))
                                     .subscribe(i -> System.out.format(
                                         "Received %d on threadName:%s\n", i,
                                         Thread.currentThread().getName()),
                                         Throwable::printStackTrace,
                                         () -> System.out.format("Completed on threadName:%s\n",
                                             Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n", Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    @Test
    public void testMultithreadedWithObserveOnScheduler() throws InterruptedException {
        System.out.format("Starting on threadName:%s\n", Thread.currentThread().getName());
        source.doOnNext(x -> System.out.format("Source on thread: %s\n", Thread.currentThread().getName()))
              .observeOn(Schedulers.computation())
              .map(x -> x + 1)
              .doOnNext(x -> System.out.format("Map on thread: %s\n", Thread.currentThread().getName()))
              .subscribe(i -> System.out.format("Received %d on threadName:%s\n", i, Thread.currentThread().getName()),
                      Throwable::printStackTrace,
                      () -> System.out.format("Completed on threadName:%s\n", Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n", Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    private void printCurrentThreadName(String marker) {
        System.out.println(marker + ":" + Thread.currentThread().getName());
    }

    @Test
    public void testWithRange() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        Observable
                .range(1, 1000000)                          //new thread
                .doOnNext(x -> printCurrentThreadName("A")) //new-thread
                .map(x -> x * 2)
                .observeOn(Schedulers.from(service))
                .doOnNext(x -> printCurrentThreadName("B")) //pool-thread
                .flatMap(x -> Observable.just(-x, x, x+3))
                .subscribeOn(Schedulers.newThread())
                .doOnNext(x -> printCurrentThreadName("C")) //pool-thread
                .filter(x -> x % 3 != 0)
                .doOnNext(x -> printCurrentThreadName("D")) //pool-thread
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        ()-> System.out.println("Completed"));

        Thread.sleep(40000);
    }

    @Test
    public void testMultithreadedWithSubscribeLongAfterTheFact() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.format("Starting on threadName:%s\n", Thread.currentThread().getName());
        source.doOnNext(x -> System.out.format("Source on thread: %s\n", Thread.currentThread().getName()))
              .observeOn(Schedulers.newThread())
              .map(x -> x + 1)
              .doOnNext(x -> System.out.format("Map on thread: %s\n", Thread.currentThread().getName()))
              .subscribeOn(Schedulers.from(executorService))
              .subscribe(i -> System.out.format("Received %d on threadName:%s\n", i, Thread.currentThread().getName()),
                      Throwable::printStackTrace,
                      () -> System.out.format("Completed on threadName:%s\n", Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n", Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    @Test
    public void testSubscriberTest() throws Exception {
        TestObserver<Integer> observer = new TestObserver<>();
        Observable.range(4, 6).subscribe(observer);
        observer.assertValues(4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testScheduler() throws Exception {
        TestScheduler testScheduler = new TestScheduler();
        TestSubscriber<Long> testSubscriber = new TestSubscriber<>();
        Flowable.interval(500, TimeUnit.MILLISECONDS, testScheduler)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        testSubscriber.assertValues(0L, 1L);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        testSubscriber.assertValues(0L, 1L, 2L, 3L);
    }

    @Test
    public void testSchedulerWithObservable() {
        TestScheduler testScheduler = new TestScheduler();
        TestObserver<Long> testObserver = new TestObserver<>();
        Observable.interval(500, TimeUnit.MILLISECONDS,testScheduler)
                .subscribeOn(testScheduler)
                .subscribe(testObserver);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        testObserver.assertValues(0L, 1L);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        testObserver.assertValues(0L, 1L, 2L, 3L);
    }
}
