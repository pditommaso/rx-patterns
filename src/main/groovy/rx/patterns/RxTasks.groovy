package rx.patterns

import java.util.concurrent.CountDownLatch

import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class RxTasks {

    static void main(String... args) {
        new RxTasks().run()
    }

    void run() {
        def latch = new CountDownLatch(2)
        Observable observable = PublishSubject.create()
        Observable tasks = observable
                .map { it ->  log.info("shared map: $it");  "task($it)" }
                .share()

        tasks
                .map { it ->log.info("map1: $it"); "do this: $it" }
                .doOnComplete({ latch.countDown() })
                .subscribe({ log.info "println1 $it" }, { e -> log.error "Fuck: $e" })
        tasks
                //.subscribeOn(Schedulers.computation())
                .map { it -> log.info("map2: $it");  "do that: $it" }
                .subscribeOn(Schedulers.computation())
                .subscribe({ log.info "println2 $it" }, { }, { latch.countDown() })
                .dispose()

        observable.onNext('Hello')
        observable.onNext('Hola')
        observable.onNext('Ciao')
        observable.onNext('Bonjour')
        observable.onNext('Fooo')
        observable.onComplete()
        
        latch.await()
    }

    static class OperatorSuppressError<T> implements ObservableOperator<T, T> {

        @Override
        Observer<? super T> apply(@NonNull Observer<? super T> target) throws Exception {
            return new Observer<T>() {
                @Override
                void onSubscribe(@NonNull Disposable disposable) {
                    target.onSubscribe(disposable)
                }

                @Override
                void onNext(@NonNull T next) {
                    target.onNext(next)
                }

                @Override
                void onError(@NonNull Throwable e) {
                    log.debug "Fucking error $e"
                }

                @Override
                void onComplete() {
                    target.onComplete()
                }
            }
        }
    }
}
