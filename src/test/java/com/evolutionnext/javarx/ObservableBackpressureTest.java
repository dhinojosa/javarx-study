package com.evolutionnext.javarx;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("Duplicates")
public class ObservableBackpressureTest {

    private Observable<Integer> crazedObservable;
    private Flowable<Integer> crazedFlowableError;
    private Flowable<Integer> crazedFlowableBackPressuredDrop;
    private Flowable<Integer> crazedFlowableBackPressuredBuffer;
    private Flowable<Integer> crazedFlowableBackPressuredLatest;

    @Before
    public void startUp() {
        crazedObservable = Observable.create
                (new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e)
                    throws Exception {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    e.onNext(i);
                    i++;
                }
            }
        });

        crazedFlowableError = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    e.onNext(i);
                    i++;
                }
            }
        }, BackpressureStrategy.ERROR);


        crazedFlowableBackPressuredDrop = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    e.onNext(i);
                    i++;
                }
            }
        }, BackpressureStrategy.DROP);

        crazedFlowableBackPressuredBuffer = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    e.onNext(i);
                    i++;
                }
            }
        }, BackpressureStrategy.BUFFER);

        crazedFlowableBackPressuredLatest = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                int i = 0;
                //noinspection InfiniteLoopStatement
                while (true) {
                    e.onNext(i);
                    i++;
                }
            }
        }, BackpressureStrategy.LATEST);
    }

    @Test
    public void testBackPressureObservable()
            throws InterruptedException {
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
    public void testBackPressureFlowableError() throws InterruptedException {
        crazedFlowableError
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
    public void testBackPressureWithDropFlowable() throws InterruptedException {
        crazedFlowableBackPressuredDrop.observeOn(Schedulers.newThread())
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
    public void testBackPressureWithBufferFlowable() throws InterruptedException {
        crazedFlowableBackPressuredBuffer.observeOn(Schedulers.newThread())
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
    public void testBackPressureWithBufferLatest() throws InterruptedException {
        crazedFlowableBackPressuredLatest.observeOn(Schedulers.newThread())
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
}
