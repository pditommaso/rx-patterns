package rx.patterns

import groovy.util.logging.Slf4j
import io.reactivex.Observable

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
class Rx4 {

    private static List<String> SUPER_HEROES = Arrays.asList(
            "Superman",
            "Batman",
            "Aquaman",
            "Asterix",
            "Captain America"
    );

    static void main(String... args) {
        Observable.fromIterable(SUPER_HEROES)
                .doOnNext( { s -> log.info "Next >> $s" })
                .doOnComplete( { log.info "Done" })
                .subscribe();
    }

}