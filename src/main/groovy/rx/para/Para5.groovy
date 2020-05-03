package rx.para

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Para5 {

    static void main(String[] args) {

        def executor = Executors.newCachedThreadPool()
        def latch = new CountDownLatch(2)
        def elems = ['one','two','three','four']
        def receiver = Observable.fromIterable(elems)
                .toFlowable(BackpressureStrategy.BUFFER)
                .map { log.info "para $it"; sleep 500; it }
                .share()

        // save tasks
        receiver.buffer(3)
                .filter { it.size()>0 }
                .doOnComplete { latch.countDown() }
                .subscribe { el-> executor.submit { log.info "saving >> $el" } }

        // aggregate metrics
        receiver.buffer(3)
                .filter { it.size()>0 }
                .doOnComplete { latch.countDown() }
                .subscribe { el-> executor.submit { log.info "metrics >> $el" } }

        latch.await()
        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)
    }
}
