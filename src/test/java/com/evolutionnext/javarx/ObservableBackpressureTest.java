package com.evolutionnext.javarx;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;


public class ObservableBackpressureTest {

    private Observable<Integer> crazedObservable;

    @Before
    public void startUp() {
        crazedObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> s) {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    s.onNext(i);
                    i++;
                }
            }
        });
    }

    @Test
    public void testBackPressure() throws InterruptedException {
        crazedObservable
                .observeOn(Schedulers.newThread())
                .subscribe(n -> {
            try {
                Thread.sleep(5); //Wait to fill the buffer some more.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(n);
        }, Throwable::printStackTrace);

        Thread.sleep(10000);
    }

    @Test
    public void testBackPressureWithSample() throws InterruptedException {
        crazedObservable.sample(250, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(n -> {

            try {
                Thread.sleep(5); //Wait to fill the buffer some more.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(n);
        }, Throwable::printStackTrace);

        Thread.sleep(10000);
    }

    @Test
    public void testBackPressureWithOnBackPressureBuffer() throws InterruptedException {
        crazedObservable.onBackpressureBuffer()
                .observeOn(Schedulers.newThread()).subscribe(n -> {
            try {
                Thread.sleep(5); //Wait to fill the buffer some more.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(n);
        }, Throwable::printStackTrace);
        Thread.sleep(10000);
    }

    @Test
    public void testBackPressureWithOnBackpressureDrop() throws InterruptedException {
        crazedObservable.onBackpressureDrop()
                .observeOn(Schedulers.newThread())
                .subscribe(n -> {
            try {
                Thread.sleep(5); //Wait to fill the buffer some more.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(n);
        }, Throwable::printStackTrace);

        Thread.sleep(10000);
    }

    @Test
    public void testBackPressureWithOnBackpressureLatest() throws InterruptedException {
        crazedObservable.onBackpressureLatest()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread()).subscribe(n -> {
            try {
                Thread.sleep(5); //Wait to fill the buffer some more.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(n);
        }, Throwable::printStackTrace);

        Thread.sleep(10000);
    }
}
