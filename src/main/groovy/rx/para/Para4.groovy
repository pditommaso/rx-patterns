package rx.para

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Para4 {

    static void main(String[] args) {
        def latch = new CountDownLatch(1)
        PublishSubject subject = PublishSubject.create()

        subject
                .toFlowable(BackpressureStrategy.BUFFER)
                .parallel()
                .runOn( Schedulers.io() )
                .map { log.info "para $it"; sleep 500; it }
                .sequential()
                .buffer(100, TimeUnit.MILLISECONDS, 3)
                .filter { it.size()>0 }
                .doOnComplete { latch.countDown() }
                .subscribe { log.info "$it" }

        subject.onNext('a')
        subject.onNext('b')
        subject.onNext('c')
        subject.onNext('d')
        subject.onComplete()
        latch.await()
}
}
