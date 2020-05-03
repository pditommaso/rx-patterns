package rx.para


import java.util.concurrent.Phaser
import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import rx.ThreadPools
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Para7 {

    static void main(String[] args) {

        def executor = ThreadPools.io('foo', 10,100,1000)
        def phaser = new Phaser()

        phaser.bulkRegister(3)

        // use rx parallel
        def receiver = Observable.fromIterable(1..1_000_000)
                .toFlowable(BackpressureStrategy.BUFFER)
                .parallel()
                .runOn( Schedulers.from(executor) )
                .map { log.info "para $it"; sleep 400; it }
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

        log.info("Await completion")
        phaser.arriveAndAwaitAdvance()

        log.info("Shutdown")
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
        log.info("Done")
    }
}
