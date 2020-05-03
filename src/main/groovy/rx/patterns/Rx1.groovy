package rx.patterns


import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull

/**
 * http://escoffier.me/rxjava-hol/#_schedulers_and_concurrency
 */
class Rx1 {

    static void main(String... args) {
        def observable = Observable.create(  new ObservableOnSubscribe() {
            @Override
            void subscribe(@NonNull ObservableEmitter emitter) throws Exception {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
            }
        } )

        observable.subscribe( {
            println "Event -> $it"
        })

    }
}