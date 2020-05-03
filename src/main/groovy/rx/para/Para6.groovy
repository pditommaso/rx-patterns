package rx.para

import java.util.concurrent.CountDownLatch
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
class Para6 {

    static void main(String[] args) {

        def executor = ThreadPools.io('foo', 10,10,10)
        def latch = new CountDownLatch(2)
        def elems = ['one','two','three','four']

        // use rx parallel
        def receiver = Observable.fromIterable(1..50)
                .toFlowable(BackpressureStrategy.BUFFER)
                .parallel()
                .runOn( Schedulers.from(executor) )
                .map { log.info "para $it"; sleep 2_000; it }
                .sequential()
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
