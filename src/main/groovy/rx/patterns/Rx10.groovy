package rx.patterns

import java.util.concurrent.CountDownLatch

import groovy.util.logging.Slf4j
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@Slf4j
class Rx10 {

    private static List<String> SUPER_HEROES = Arrays.asList(
            "Superman",
            "Batman",
            "Aquaman",
            "Asterix",
            "Captain America"
    );

    static void main(String[] args) {
        def latch = new CountDownLatch(1)

        Observable<Object> observable = Observable.create({ emitter ->
            for (String superHero : SUPER_HEROES) {
                log.info("Emitting: " + superHero);
                emitter.onNext(superHero);
            }
            log.info("Completing")
            emitter.onComplete()
        });


        // when using `subscribeOn` both the emitter and the subscribe
        // runs on a separate threa
        // 
        // d (pool)
        observable
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {item -> log.info("Received " + item)},
                        {error -> log.info("Error")},
                        { -> log.info("Complete"); latch.countDown() } )
        log.info("---------------- Subscribed");

        // note the use of a `CountDownLatch` to await the completion
        latch.await()
    }
}
