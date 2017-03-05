package com.evolutionnext.javarx;


import io.reactivex.Flowable;
import io.reactivex.Notification;
import org.junit.Test;
import io.reactivex.Observable;

public class ObservableMaterializeTest {
    @Test
    public void testObservableMaterializeDematerialize() {
        Observable.range(-10, 20)
                .map(x -> 20 / x)
                .materialize()
                .filter(Notification::isOnNext)
                .dematerialize()
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testFlowableMaterializeDematerialize() {
        Flowable.range(-10, 20)
                .map(x -> 20 / x)
                .materialize()
                .filter(Notification::isOnNext)
                .dematerialize()
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
