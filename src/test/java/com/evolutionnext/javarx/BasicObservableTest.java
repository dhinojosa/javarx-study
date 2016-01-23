package com.evolutionnext.javarx;

import com.sun.javafx.binding.StringFormatter;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func0;
import rx.observables.StringObservable;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Daniel Hinojosa
 * @since 1/12/16 9:16 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
public class BasicObservableTest {

    @Test
    public void testBasic() throws InterruptedException {
        Observable.just(1, 2, 3).subscribe(System.out::println);
        Thread.sleep(2000);
    }

    @Test
    public void testDefer() throws InterruptedException {
        Observable<LocalTime> localTimeObservable =
                Observable.defer(() -> Observable
                        .just(LocalTime.now())).repeat(3);
        localTimeObservable.subscribe(System.out::println);
        Thread.sleep(3000);
        localTimeObservable.subscribe(System.out::println);
    }


    @Test
    public void testTicker() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};
        Observable<String> stringObservable = Observable.from(ticker);
        stringObservable.flatMap(s ->
                Observable.from(
                        TickerPriceFinder.create()
                                .getPrice(s))).subscribe(System.out::println);
    }

    @Test
    public void testTickerToList() throws InterruptedException {
        String[] ticker = {"MSFT", "GOOG", "YHOO", "APPL"};

        Observable<String> stringObservable = Observable.from(ticker);
        StringObservable.join(
                stringObservable.flatMap(s ->
                     Observable.from(
                        TickerPriceFinder.create().getPrice(s)))
                           .map(d -> String.format("%.2f", d)), ",")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
