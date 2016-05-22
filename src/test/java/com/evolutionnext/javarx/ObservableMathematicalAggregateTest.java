package com.evolutionnext.javarx;

import com.github.davidmoten.rx.Functions;
import org.junit.Test;
import rx.Observable;
import rx.math.operators.OperatorMinMax;
import rx.observables.MathObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The following tests require the rxjava-math package
 */
public class ObservableMathematicalAggregateTest {

    @Test
    public void testAverages() {
        MathObservable.from(Observable.range(1, 10))
                .averageInteger(Functions.identity())
                .subscribe(System.out::println);
    }

    @Test
    public void testAveragesLong() {
        MathObservable.from(Observable.range(5, 22))
                .averageLong(Long::new)
                .subscribe(System.out::println);
    }

    @Test
    public void testAveragesFloat() {
        MathObservable.from(Observable.range(5, 22))
                .averageFloat(Float::new)
                .subscribe(System.out::println);
    }

    @Test
    public void testAveragesDouble() {
        MathObservable.from(Observable.range(5, 22))
                .averageDouble(Double::new)
                .subscribe(System.out::println);
    }

    @Test
    public void maxBy() {
        List list1 = Arrays.asList(1, 2, 4, 5, 6);
        List list2 = Arrays.asList(1, 2, 4);
        List list3 = Arrays.asList(1, 2);
        List list4 = Arrays.asList(1, 2, 10, 11, 16, 44, 20, 19);
        Observable<List<List>> listObservable =
                OperatorMinMax.maxBy
                        (Observable.just(list1, list2, list3, list4),
                                List::size);
        listObservable.subscribe(System.out::println);
    }

    @Test
    public void testCount() {
        Observable.just(1, 5, 6, 4, 10, 22).count().subscribe(System.out::println);
    }

    @Test
    public void testReduce() {
        Observable.range(1, 5).reduce((next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testReduceWithSeed() {
        Observable.range(1, 5).reduce(10, (next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testScan() {
        Observable.range(1, 5).scan((next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testScanWithSeed() {
        Observable.range(1, 5).scan(10, (next, total) -> total * next)
                .subscribe(System.out::println);
    }

    @Test
    public void testCollect() {
        Observable.range(1, 5).collect(
                () -> new ArrayList<>(),
                (list, item) -> {list.add(item);})
                .subscribe(System.out::println);
    }
}
