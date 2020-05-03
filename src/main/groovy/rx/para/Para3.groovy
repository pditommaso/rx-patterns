package rx.para

import java.util.concurrent.CountDownLatch

import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Para3 {


    // madness
    // https://medium.com/@softjake/rxjava2-schedulers-101-or-simplified-concurrency-management-40ab0ed1ce1d
    // https://medium.com/@softjake/rxjava2-schedulers-101-or-simplified-concurrency-part-2-2b67442533cb

    static void main(String... args) {
        def latch = new CountDownLatch(1)
        PublishSubject subject = PublishSubject.create()
        subject
                .flatMap( {
                    Observable
                            .just(it)
                            .subscribeOn(Schedulers.io())
                            .map { log.info "long job: $it"; sleep 500; it }
                } )
                .doOnComplete { latch.countDown() }
                .subscribe { log.info("subscribe: $it")  }

        subject.onNext('a')
        subject.onNext('b')
        subject.onNext('c')
        subject.onNext('d')
        subject.onComplete()
        latch.await()
    }

}
