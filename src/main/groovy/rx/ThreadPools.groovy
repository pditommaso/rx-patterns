package rx

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

import groovy.transform.CompileStatic
/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@CompileStatic
class ThreadPools {

    static io(String name) {
        io(name , 10, 100, 10_000)
    }

    static io(String name, int min, int max, int queue, long timeout=60_000) {
        new ThreadPoolExecutor(
                min,
                max,
                timeout, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(queue),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy())
    }

}