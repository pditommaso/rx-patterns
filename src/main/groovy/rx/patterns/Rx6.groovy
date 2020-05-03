package rx.patterns

import groovy.util.logging.Slf4j
import io.reactivex.Observable

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Rx6 {

    static void main(String... args) {
        def stream = Observable.create( { subscriber ->
            sleep 50
            subscriber.onNext(1)
            subscriber.onNext(2)
            subscriber.onNext(3)
            subscriber.onComplete()
        })
                .publish()
                .autoConnect()
                .doOnSubscribe( { log.warn "Somebody subscribe" } )
                .doOnDispose( { log.warn "Somebody left" } )

        def s1 = stream.subscribe { log.info "[A] => $it" }
        sleep 100
        s1.dispose()

        // this is `lost` because it subscribes after the emission completes
        def s2 = stream.subscribe { log.info "[B] => $it" }
        sleep 100
        s2.dispose()
    }
}
