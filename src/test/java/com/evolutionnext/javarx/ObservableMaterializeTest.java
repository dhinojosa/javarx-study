package com.evolutionnext.javarx;


import org.junit.Test;
import rx.Notification;
import rx.Observable;

public class ObservableMaterializeTest {
   @Test
   public void testMaterializeDematerialize() {
       Observable.range(-10, 20)
               .map(x -> 20 / x)
               .materialize()
               .filter(Notification::isOnNext)
               .dematerialize()
               .subscribe(System.out::println, Throwable::printStackTrace);
   }
}
