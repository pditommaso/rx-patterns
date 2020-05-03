package rx.para

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class Para2 {


    // this is sequential!!

    static void main(String[] args) {
        def latch = new CountDownLatch(1)
        PublishSubject subject = PublishSubject.create()
        subject
                .observeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .doOnComplete { latch.countDown() }
                .subscribe { sleep 500; log.info "$it" }

        subject.onNext('helo')
        subject.onNext('world')
        subject.onComplete()
        latch.await()
    }
}
