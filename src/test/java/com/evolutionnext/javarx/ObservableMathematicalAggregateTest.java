package com.evolutionnext.javarx;

import com.github.davidmoten.rx.Functions;
import org.junit.Test;
import rx.Observable;
import rx.observables.MathObservable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public void testConcat() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Observable<Integer> observable1 = Observable
                .from(executorService.submit(() -> {
                    System.out.format
                            ("In Observable 1: In Thread %s Going To Sleep\n",
                                    Thread.currentThread().getId());
                    Thread.sleep(5000);
                    System.out.format
                            ("In Observable 1: In Thread %s Waking Up\n",
                                    Thread.currentThread().getId());
                    return 3000;
                }));

        Observable<Integer> observable2 = Observable
                .from(executorService.submit(() -> {
                    System.out.format(
                            "In Observable 2: In Thread %s Going To Sleep\n",
                            Thread.currentThread().getId());
                    Thread.sleep(1000);
                    System.out.format(
                            "In Observable 2: In Thread %s Waking Up\n",
                            Thread.currentThread().getId());
                    return 9000;
                }));

        Observable.concat(observable1, observable2)
                .subscribe(System.out::println);
    }


    @Test
    public void testCount() {
        Random random = new Random();
        Observable<Integer> randomObservable = Observable.fromCallable(() -> 1 + random.nextInt(49));
        randomObservable.flatMap(x -> Observable.range(0, x)).count().subscribe(System.out::println);
    }


    //http://www.gutenberg.org/cache/epub/2600/pg2600.txt
}
