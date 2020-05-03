package rx.para


import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Para1 {

    // https://medium.com/upday-devs/rxjava-subscribeon-vs-observeon-9af518ded53a

    static void main(String... args) {
        Observable.just("Some String") // Computation
                .map({str -> str.length()}) // Computation
                .map({length -> 2 * length}) // Computation
                .subscribeOn(Schedulers.io()) // -- changing the thread
                .subscribe({ log.debug "$it" });// Computation
        
        sleep(100)
    }
}
