package rx.patterns

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class RxGroupBy {

    static void main(String... args) {
        new RxGroupBy().run()
    }

    void run() {
        def latch = new CountDownLatch(1)
        Observable observable = PublishSubject.<String>create()
//        observable
//                .groupBy { String it -> it[0] }
//                .map { group ->  group.buffer(1, TimeUnit.SECONDS).toList() }
//                .subscribe({ println it }, { }, { latch.countDown() })

        observable
                .buffer(1, TimeUnit.SECONDS, 100)
                .flatMapIterable { it.groupBy { str->str[0] }.values() }
                .subscribe({ println it }, { }, { latch.countDown() })

        def abc = ['a','b','c']
        def rnd = new Random()
        def ts = System.currentTimeMillis()
        while( System.currentTimeMillis()-ts < 5_000 ) {
            def prefix = abc [ rnd.nextInt(3)]
            observable.onNext(prefix + rnd.nextInt(10))
            sleep 75
        }
        observable.onComplete()
        
        latch.await()
    }


}
