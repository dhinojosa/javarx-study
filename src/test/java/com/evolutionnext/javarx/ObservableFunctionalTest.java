package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;
import rx.observables.GroupedObservable;


/**
 * Demo 2: Standard Functional Operators
 */
public class ObservableFunctionalTest {

    @Test
    public void testFilter() {
        Observable<Integer> observable =
                Observable.range(10, 20);

        Observable<Integer> filtered = observable.filter(integer -> integer % 2 == 0);
        filtered.subscribe(System.out::println);
    }

    @Test
    public void testMap() {
        Observable<String> observable =
                Observable.range(1, 10)
                        .map(x -> "Wow" + (x + 3));

        observable.subscribe(System.out::println);
    }

    @Test
    public void testFlatMap() {
        Observable<Integer> observable =
                Observable.range(1, 10)
                        .flatMap(x -> Observable.just(x - 1, x, x + 1));

        observable.subscribe(System.out::println);
    }

    @Test
    public void testBasicGroupBy() throws InterruptedException {
        Observable<GroupedObservable<String, Integer>> grouped =
                Observable.range(1, 100).groupBy(x -> {
                    if (x % 2 == 0) {
                        return "even";
                    } else {
                        return "odd";
                    }
                });

        grouped.subscribe(g -> {
            g.subscribe(x -> System.out.println("g:" + g.getKey() + ", value:" + x));
        });

        Thread.sleep(4000);
    }
}
