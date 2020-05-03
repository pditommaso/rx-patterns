package rx.patterns


import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class Rx8 {

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
            log.info("Completing");
            emitter.onComplete();
        });

        log.info("---------------- Subscribing");
        observable.subscribe(
                {item -> log.info("Received " + item)},
                {error -> log.info("Error")},
                { -> log.info("Complete") } )
        log.info("---------------- Subscribed");
    }
}