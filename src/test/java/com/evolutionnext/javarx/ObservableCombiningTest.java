package com.evolutionnext.javarx;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Hinojosa
 * @since 3/19/16 10:08 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class ObservableCombiningTest {

    @Test
    public void testStartWith() throws InterruptedException {
        Observable<Integer> observable1 = Observable.range(10, 5);
        observable1.startWith(1,2,3).subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void testMerge() throws InterruptedException {
        Observable<String> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(x -> "O1:" + x).take(5);
        Observable<String> observable2 = Observable.interval(150, TimeUnit.MILLISECONDS)
                .map(x -> "O2:" + x).take(5);
        observable1.mergeWith(observable2).subscribe(System.out::println);
        Thread.sleep(20000);
    }

    @Test
    public void testConcat() throws InterruptedException {
        Observable<String> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(x -> "O1:" + x).take(5);
        Observable<String> observable2 = Observable.interval(150, TimeUnit.MILLISECONDS)
                .map(x -> "O2:" + x).take(5);
        observable1.concatWith(observable2).subscribe(System.out::println);
        Thread.sleep(20000);
    }

    @Test
    public void testSwitchOnNext() throws InterruptedException {
        Observable<String> observable1 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(x -> "O1:" + x).take(5);
        Observable<String> observable2 = Observable.interval(150, TimeUnit.MILLISECONDS)
                .map(x -> "O2:" + x).delay(50, TimeUnit.MILLISECONDS).take(5);
        Observable<String> observable3 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(x -> "O3:" + x).delay(80, TimeUnit.MILLISECONDS).take(5);
        Observable<Observable<String>> observable4 =
                Observable.just(observable1, observable2, observable3);
        Observable.switchOnNext(observable4).subscribe(System.out::println);
        Thread.sleep(20000);
    }
}
