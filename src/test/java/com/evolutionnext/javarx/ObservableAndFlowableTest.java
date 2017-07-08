package com.evolutionnext.javarx;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Test;

public class ObservableAndFlowableTest {

    @Test
    public void testAssertionsOnObservables() throws Exception {
        Observable<Integer> observable = Observable.just(1, 2, 3).map(x -> x + 3);
        TestObserver<Integer> testObserver = new TestObserver<>();
        observable.subscribe(testObserver);
        testObserver.assertValues(4, 5, 6);
    }

    @Test
    public void testAssertionsOnObservablesWithTest() throws Exception {
        Observable<Integer> observable = Observable.just(1, 2, 3).map(x -> x + 3);
        TestObserver<Integer> testObserver = observable.test();
        testObserver.assertValues(4, 5, 6);
    }

    @Test
    public void testAssertionsOnSubscriber() throws Exception {
        Flowable<Integer> flowable = Flowable.just(1, 2, 3).map(x -> x + 3);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        flowable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValues(4,5,6);
    }

    @Test
    public void testAssertionsWithFlowableSubscriber() throws Exception {
        Flowable<Integer> flowable = Flowable.just(1, 2, 3).map(x -> x + 3);
        TestSubscriber<Integer> testSubscriber = flowable.test();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValues(4,5,6);
        Thread.sleep(4000);
    }
}
