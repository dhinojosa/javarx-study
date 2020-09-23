package com.evolutionnext.javarx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Notification;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;


public class ObservableMaterializeTest {
    @Test
    public void testObservableMaterializeDematerialize() {
        Observable.range(-10, 20)
                  .map(x -> 20 / x)
                  .materialize()
                  .filter(Notification::isOnNext)
                  .dematerialize(ni -> ni)
                  .subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void testFlowableMaterializeDematerialize() {
        Flowable.range(-10, 20)
                .map(x -> 20 / x)
                .materialize()
                .filter(Notification::isOnNext)
                .dematerialize(ni -> ni)
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
