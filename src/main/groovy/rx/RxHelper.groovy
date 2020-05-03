package rx

import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class RxHelper {
    /**
     * Helper method to sync with the completion of an ob
     * @param observable
     */
    static void await(Observable observable, Duration timeout=null) {
        final latch = new CountDownLatch(1)
        observable.subscribeWith( new Observer() {

            @Override
            void onComplete() {
                latch.countDown()
            }

            @Override
            void onError(@NonNull Throwable e) {
                latch.countDown()
            }

            @Override
            void onSubscribe(@NonNull Disposable d) { }

            @Override
            void onNext(@NonNull Object o) { }

        })

        // await completion
        timeout!=null ? latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS) : latch.await()
    }


}