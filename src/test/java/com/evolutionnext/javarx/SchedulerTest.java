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
                    System.out.format("Invoked on threadId:%d\n", Thread.currentThread().getId());
                    o.onNext(1);
                    o.onNext(2);
                    o.onNext(3);
                    o.onCompleted();
                    System.out.format("Finished on threadId:%d\n", Thread.currentThread().getId());
                }
        );
    }

    @Test
    public void testProofThatAllIsSingleThreaded() throws InterruptedException {
        //RX is single threaded
        //Translated from: http://www.introtorx.com/Content/v1.0.10621.0/15_SchedulingAndThreading.html
        System.out.format("Starting on threadId:%d\n", Thread.currentThread().getId());

        source.subscribe(i ->
                System.out.format("Received %d on threadId:%d\n", i, Thread.currentThread().getId()),
                Throwable::printStackTrace,
                () -> System.out.format("Completed on threadId:%d\n", Thread.currentThread().getId()));
        Thread.sleep(2000);
    }

    @Test
    public void testMultithreadedWithScheduler() throws InterruptedException {
        System.out.format("Starting on threadId:%d\n", Thread.currentThread().getId());
        source
                .subscribeOn(Schedulers.newThread())
                .subscribe(i ->
                        System.out.format("Received %d on threadId:%d\n",
                                i, Thread.currentThread().getId()),
                Throwable::printStackTrace,
                () -> System.out.format("Completed on threadId:%d\n",
                        Thread.currentThread().getId()));

        Thread.sleep(2000);
    }

    @Test
    public void testSchedulerElsewhere() throws InterruptedException {
        System.out.format("Starting on threadId:%d\n", Thread.currentThread().getId());
        source
                .subscribeOn(Schedulers.newThread())
                .subscribe(i ->
                                System.out.format("Received %d on threadId:%d\n",
                                        i, Thread.currentThread().getId()),
                        Throwable::printStackTrace,
                        () -> System.out.format("Completed on threadId:%d\n",
                                Thread.currentThread().getId()));

        Thread.sleep(2000);
    }
}