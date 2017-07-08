package com.evolutionnext.javarx;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.Test;
import rx.observables.MathObservable;

import java.util.ArrayList;

/**
 * The following tests require the rxjava-math package
 */
public class ObservableMathematicalAggregateTest {

    @Test
    public void testAveragesUsingRawRxJava() {
        Observable<Integer> range = Observable.range(1, 10);
        Maybe<Integer> reduce = range.reduce((next, total) -> total + next);
        Maybe<Long> result =
                reduce.flatMap(total -> range.count().map(count -> total / count).toMaybe());
        System.out.println(result.blockingGet());
    }

    @Test
    public void testCountObservable() {
        Observable.just(1, 5, 6, 4, 10, 22).count().subscribe(System.out::println);
    }

    @Test
    public void testCountFlowable() {
        Flowable.just(1, 5, 6, 4, 10, 22).count().subscribe(System.out::println);
    }

    @Test
    public void testReduceObservable() {
        Maybe<Integer> reduce =
                Observable.range(1, 5).reduce((total, next) -> {
            System.out.print("next:" + next);
            System.out.print(" ");
            System.out.println("total:" + total);
            return total * next;
        });

        reduce.subscribe(System.out::println);
    }

    @Test
    public void testReduceFlowable() {
        Flowable.range(1, 5).reduce((total, next) -> {
            System.out.print("next:" + next);
            System.out.print(" ");
            System.out.println("total:" + total);
            return total * next;
        }).subscribe(System.out::println);
    }

    @Test
    public void testReduceObservableWithSeed() {
        Observable.range(1, 5)
                .reduce(0, (total, next) -> {
                    System.out.print("next:" + next);
                    System.out.print(" ");
                    System.out.println("total:" + total);
                    return total + next;
                }).subscribe(System.out::println);
    }

    @Test
    public void testReduceFlowableWithSeed() {
        Flowable.range(1, 5)
                .reduce(0, (total, next) -> {
                    System.out.print("next:" + next);
                    System.out.print(" ");
                    System.out.println("total:" + total);
                    return total + next;
                }).subscribe(System.out::println);
    }

    @Test
    public void testScanObservable() {
        Observable.range(1, 5).scan((next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testScanFlowable() {
        Flowable.range(1, 5).scan((next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testScanWithSeedObservable() {
        Observable.range(1, 5).scan(10, (next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testScanWithSeedFlowable() {
        Flowable.range(1, 5).scan(10, (next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testCollectObservable() {
        Observable.range(1, 5).collect(
                ArrayList::new,
                ArrayList::add)
                .subscribe(System.out::println);
    }

    @Test
    public void testCollectFlowable() {
        Flowable.range(1, 5).collect(
                ArrayList::new,
                ArrayList::add)
                .subscribe(System.out::println);
    }
}
