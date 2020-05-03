package rx.para


import java.util.concurrent.Phaser
import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rx.ThreadPools
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Beanch8 {


    static def executor = ThreadPools.io('foo', 10,100,1000)
    static def phaser = new Phaser()

    static PublishSubject makeSubject() {

        phaser.bulkRegister(2)

        PublishSubject subject = PublishSubject.create()

        def receiver = subject
                .toFlowable(BackpressureStrategy.BUFFER)
                .parallel()
                .runOn( Schedulers.from(executor) )
                .map { log.info "para $it"; sleep 100; it }
                .sequential()
                .share()

        // save tasks
        receiver.buffer(3)
                .filter { it.size()>0 }
                .doOnComplete { phaser.arriveAndDeregister() }
                .subscribe { el-> executor.submit { log.info "saving >> $el" } }

        // aggregate metrics
        receiver.buffer(3)
                .filter { it.size()>0 }
                .doOnComplete { phaser.arriveAndDeregister() }
                .subscribe { el-> executor.submit { log.info "metrics >> $el" } }

        return subject
    }

    static fromList(List items) {
        def s = makeSubject()
        for( def it : items )
            s.onNext(it)
        s.onComplete()
    }

    static void main(String[] args) {

        phaser.register()

        // produce
        1_000.times { fromList( new IntRange(it * 1_000, it * 1_000+1_000) ) }


        log.info "Await completion"
        phaser.arriveAndAwaitAdvance()

        log.info "Shutdown"
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
        log.info "Done"
    }
}
