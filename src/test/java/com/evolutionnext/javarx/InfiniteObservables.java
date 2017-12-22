package com.evolutionnext.javarx;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class InfiniteObservables {
    @Test
    public void testBadInfinitiveSameThread() throws Exception {
        Observable<BigInteger> counters = Observable.create(
                subscriber -> {
                    BigInteger i = ZERO;
                    while (true) {
                        subscriber.onNext(i);
                        i = i.add(ONE);
                    }
                });
        counters.subscribe(System.out::println);
    }

    Observable<BigInteger> countersThread = Observable.create(
            subscriber -> {
                Runnable runnable = () -> {
                    BigInteger i = ZERO;
                    while (!(subscriber.isDisposed())) {
                        subscriber.onNext(i);
                        i = i.add(ONE);
                    }
                };
                new Thread(runnable).start();
            });

    @Test
    public void testOkInfinitiveDifferentThread() throws Exception {


        countersThread.subscribeWith(new Observer<BigInteger>() {
            private Disposable d;

            @Override
            public void onSubscribe(Disposable d) {
                this.d = d;

            }

            @Override
            public void onNext(BigInteger bigInteger) {
                System.out.println(bigInteger);
                if (bigInteger.compareTo(new BigInteger("2000")) == 0) d.dispose();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });

        Thread.sleep(10000);
    }

    @Test
    public void testOkInfinitiveDifferentThreadWithObserverDisposable() throws Exception {
        countersThread.subscribeWith(new DisposableObserver<BigInteger>() {
            @Override
            public void onNext(BigInteger bigInteger) {
                System.out.println(bigInteger);
                if (bigInteger.compareTo(new BigInteger("2000")) == 0) dispose();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });

        Thread.sleep(10000);
    }

    Observable<BigInteger> countersThreadListener = Observable.create(
            subscriber -> {
                Runnable runnable = () -> {
                    BigInteger i = ZERO;
                    while (!(subscriber.isDisposed())) {
                        subscriber.onNext(i);
                        i = i.add(ONE);
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);

                subscriber.setCancellable(() -> System.out.println("Cancelled"));
                subscriber.setDisposable(new Disposable() {
                    private boolean flag = false;
                    @Override
                    public void dispose() {
                        System.out.println("Disposed!");
                        flag = true;
                    }

                    @Override
                    public boolean isDisposed() {
                        return flag;
                    }
                });
                thread.start();
            });

    @Test
    public void testCountThreadsListenerWithoutWaitingThatLong() throws Exception {

        countersThread.subscribeWith(new DisposableObserver<BigInteger>() {
            @Override
            public void onNext(BigInteger bigInteger) {
                System.out.println(bigInteger);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Disposing");
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });

        Thread.sleep(30000);
    }
}
