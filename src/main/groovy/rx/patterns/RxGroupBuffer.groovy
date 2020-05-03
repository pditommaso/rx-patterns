package rx.patterns

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class RxGroupBuffer {

    static void main(String[] args) {

        int items=0
        def latch = new CountDownLatch(1)
        Observable observable = PublishSubject.<String>create()
        observable
                .groupBy { String it -> it[0] }
                .flatMap { group ->  group.buffer(1, TimeUnit.SECONDS, 10) }
                .subscribe({ items+=it.size(); println it }, { }, { latch.countDown() })

        def abc = ['a','b','c']
        def rnd = new Random()
        def ts = System.currentTimeMillis()
        int count=0
        while( System.currentTimeMillis()-ts < 5_000 ) {
            def prefix = abc [ rnd.nextInt(3)]
            def elem = prefix + rnd.nextInt(10)
            //println elem
            observable.onNext(elem)
            count++
            sleep 25
        }
        observable.onComplete()

        latch.await()
        assert items==count

    }
}
