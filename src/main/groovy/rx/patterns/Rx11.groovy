package rx.patterns


import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@Slf4j
class Rx11 {

    private static List<String> SUPER_HEROES = Arrays.asList(
            "Superman",
            "Batman",
            "Aquaman",
            "Asterix",
            "Captain America"
    );

    static void main(String[] args) {

        Observable<Object> observable = Observable.create({ emitter ->
            for (String superHero : SUPER_HEROES) {
                log.info("Emitting: " + superHero);
                emitter.onNext(superHero);
            }
            log.info("Completing")
            emitter.onComplete()
        })

        // note when using `observeOn` only the observer runs
        // on a separate thread (pool). The emitter run in the main thread
        observable
                .observeOn(Schedulers.io())
                .subscribe(
                        {item -> log.info("Received " + item)},
                        {error -> log.info("Error")},
                        { -> log.info("Complete");  } )
        log.info("---------------- Subscribed");

        println "done"
    }
}