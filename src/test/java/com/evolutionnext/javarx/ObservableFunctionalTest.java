package com.evolutionnext.javarx;

import com.sun.tools.javac.comp.Flow;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Demo 2: Standard Functional Operators
 */
public class ObservableFunctionalTest {

    @Test
    public void testFilterObservable() {
        Observable<Integer> observable =
                Observable.range(10, 20);
        Observable<Integer> filtered =
                observable.filter(integer -> integer % 2 == 0);
        filtered.subscribe(System.out::println);
    }

    @Test
    public void testFilterFlowable() {
        Flowable<Integer> flowable =
                Flowable.range(10, 20);
        Flowable<Integer> filtered =
                flowable.filter(integer -> integer % 2 == 0);
        filtered.subscribe(System.out::println);
    }

    @Test
    public void testMapObservable() {
        Observable<String> observable =
                Observable.range(1, 10)
                        .map(x -> "Wow" + (x + 3));
        observable.subscribe(System.out::println);
    }

    @Test
    public void testMapFlowable() {
        Flowable<String> flowable =
                Flowable.range(1, 10)
                        .map(x -> "Wow" + (x + 3));
        flowable.subscribe(System.out::println);
    }

    @Test
    public void testFlatMapObservable() {
        Observable<Integer> observable =
                Observable.range(1, 10)
                        .flatMap(x -> Observable.just(x - 1, x, x + 1));

        observable.subscribe(System.out::println);
    }

    @Test
    public void testFlatMapFlowable() {
        Flowable<Integer> flowable =
                Flowable.range(1, 10)
                        .flatMap(x -> Flowable.just(x - 1, x, x + 1));
        flowable.subscribe(System.out::println);
    }

    @Test
    public void testBasicGroupByObservable() throws InterruptedException {
        Observable<GroupedObservable<String, Integer>> grouped =
                Observable.range(1, 100)
                          .groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        grouped.subscribe(g -> {
            g.subscribe(x ->
                    System.out.println
                            ("g:" + g.getKey() + ", value:" + x));
        });

        Thread.sleep(4000);
    }

    @Test
    public void testBasicGroupByFlowable() throws InterruptedException {
        Flowable<GroupedFlowable<String, Integer>> groupedFlowable =
                Flowable.range(1, 100).groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        groupedFlowable.subscribe(g -> {
            g.subscribe(x -> System.out.println("g:" + g.getKey() + ", value:" + x));
        });
        Thread.sleep(4000);
    }

    @Test
    public void testBasicGroupByFlowableReduceIntoMultiMap() throws InterruptedException {
        Flowable<GroupedFlowable<String, Integer>> groupedFlowable =
                Flowable.range(1, 100).groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        Map<String, Single<List<Integer>>> result = new HashMap<>();

        groupedFlowable.subscribe(g -> {
            result.put(g.getKey(), g.toList());
        });

        System.out.println(result.get("Even").blockingGet());
        System.out.println(result.get("Odd").blockingGet());
    }
}
