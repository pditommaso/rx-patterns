package rx.patterns

import java.util.concurrent.TimeUnit

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.annotations.NonNull
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Rx7 {

    static void main(String... args) {
        def source = new FlowableOnSubscribe() {
            @Override
            void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                emitter.onNext( 'event a' )
                emitter.onNext( 'event b' )
                emitter.onNext( 'event c' )
            }}

        Flowable
                .create(source, BackpressureStrategy.BUFFER)
                .buffer(100, TimeUnit.MILLISECONDS)
                .filter { it.size()>0 }
                .subscribe {
                    log.info "Got items=$it"
                }

        sleep 1000
    }
}
