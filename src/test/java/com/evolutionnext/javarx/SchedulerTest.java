package com.evolutionnext.javarx;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchedulerTest {

    private Observable<Integer> source;


    /**
     * Setting up a basic Observable for the two tests
     */
    @Before
    public void setUp() throws Exception {
        source = Observable.create(
                o -> {
                    System.out.format(
                            "Source begins: invoked on threadName:%s\n",
                            Thread.currentThread().getName());
                    o.onNext(1);
                    o.onNext(2);
                    o.onNext(3);
                    o.onCompleted();
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

        source.subscribe(i ->
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
        System.out.format("Starting on threadName:%s\n",
                Thread.currentThread().getName());
        source
                .subscribeOn(Schedulers.newThread())
                .subscribe(i ->
                        System.out.format("Received %d on threadName:%s\n",
                                i, Thread.currentThread().getName()),
                Throwable::printStackTrace,
                () -> System.out.format("Completed on threadName:%s\n",
                        Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n",
                Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    @Test
    public void testMultithreadedWithObserveOn() throws InterruptedException {
        System.out.format("Starting on threadName:%s\n", Thread.currentThread().getName());
        source.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .subscribe(i ->
                                System.out.format("OnNext %d on threadName:%s\n",
                                        i, Thread.currentThread().getName()),
                        Throwable::printStackTrace,
                        () -> System.out.format("Completed on threadName:%s\n",
                                Thread.currentThread().getName()));

        System.out.format("Ending on threadName:%s\n", Thread.currentThread().getName());
        Thread.sleep(2000);
    }
}