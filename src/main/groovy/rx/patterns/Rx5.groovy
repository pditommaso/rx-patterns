package rx.patterns


import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class Rx5 {

    static void main(String... args) {
        Observable<String> stream = Observable.just("Black Canary", "Catwoman", "Elektra");

        stream
                .subscribe(
                        { i -> log.info("[A] Received: " + i) },
                        { err -> log.info("[A] BOOM")},
                        { log.info("[A] Completion") }
                );


        sleep 500

        stream
                .subscribe(
                        { i -> log.info("[B] Received: " + i) },
                        { err -> log.info("[B] BOOM")},
                        { log.info("[B] Completion") }
                );

    }
}
