package com.evolutionnext.javarx;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Demo 3: Standard Functional Operators
 */
public class CombinationTest {

    @Test
    public void testStartWith() throws InterruptedException {
        Observable<Integer> observable1 = Observable.range(10, 5);
        observable1.startWith(Observable.just(1,4,5)).subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void testMerge() throws InterruptedException {
        Observable<String> observable1 =
                Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(x -> {
                    return "O1:" + x;});
        Observable<String> observable2 =
                Observable.interval(150, TimeUnit.MILLISECONDS)
                .map(x -> {
                    return "O2:" + x;});

        observable1.mergeWith(observable2)
                .subscribe(System.out::println);
        Thread.sleep(10000);
    }

    @Test
    public void testConcat() throws InterruptedException {
        Observable<String> observable1 =
                Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(x -> "O1:" + x).take(5);
        Observable<String> observable2 =
                Observable.interval(150, TimeUnit.MILLISECONDS)
                .map(x -> "O2:" + x).take(5);
        observable1.concatWith(observable2).subscribe(System.out::println);
        Thread.sleep(20000);
    }

    //Ambiguous RXJava
    @Test
    public void testAmb() throws InterruptedException {
        Observable<Integer> oneTo10 = Observable.range(1, 10)
                .delay(5, TimeUnit.SECONDS);
        Observable<Integer> tenTo20 = Observable.range(10, 10)
                .delay(2, TimeUnit.SECONDS);
        Observable<Integer> twentyTo30 = Observable.range(20, 10)
                .delay(8, TimeUnit.SECONDS);

        Observable.amb(Arrays.asList(oneTo10, tenTo20, twentyTo30))
                .subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(10000);
    }

    @Test
    public void testZip() throws InterruptedException {
        Observable<String> interval1 = Observable.fromArray("Naan", "Milk", "Sugar", "Beets");
        Observable<Integer> interval2 = Observable.range(1, Integer.MAX_VALUE);
        Observable<String> zipped = interval1.zipWith(interval2, (string1, aLong2) -> aLong2 + ". " + string1);
        zipped.subscribe(x -> System.out.println("x1 = " + x));
        zipped.subscribe(x -> System.out.println("x2 = " + x));
        TestObserver<String> testObserver = new TestObserver<>();
        zipped.subscribe(testObserver);
        testObserver.assertValues("1. Naan", "2. Milk", "3. Sugar", "4. Beets");
        Thread.sleep(10000);
    }

    @Test
    public void testSwitchOnNext() throws InterruptedException {
        Observable<Observable<String>> strings = Observable.range(0, 10)
                                                           .map(c -> Observable.range(0, 10)
                                                                               .delay(2, TimeUnit.MILLISECONDS)
                                                                               .map(d -> c + "" + d));
        Observable<String> stringObservable = Observable.switchOnNext(strings);
        stringObservable.subscribe(System.out::println);
        Thread.sleep(4000);
    }
}
