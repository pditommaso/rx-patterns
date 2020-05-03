package rx.patterns

import groovy.util.logging.Slf4j
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

@Slf4j
class Rx3 {

    static void main(String[] args) {
        def observable = PublishSubject.create()
        observable
                .observeOn(Schedulers.io())
                .subscribe( { log.info "$it" }, {},  { println 'Done' })


        observable.onNext(1)
        observable.onNext(2)
        observable.onNext(3)

        sleep 200
    }

}
