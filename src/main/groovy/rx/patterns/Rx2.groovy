package rx.patterns

import groovy.util.logging.Slf4j
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.annotations.NonNull

@Slf4j
class Rx2 {

    static void main(String... args) {
        def observable = Flowable.create(  new FlowableOnSubscribe() {
            @Override
            void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(2)
                emitter.onNext(3)
            }
        }, BackpressureStrategy.BUFFER )

        observable.subscribe( {
            log.info "Event -> $it"
        })
    }
}