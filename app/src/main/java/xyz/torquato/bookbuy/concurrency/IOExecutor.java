package xyz.torquato.bookbuy.concurrency;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IOExecutor implements Executor {

    private final Executor simpleExecutor;

    @Inject
    public IOExecutor(Executor executor) {
        this.simpleExecutor = executor;
    }

    @Override
    public void execute(Runnable command) {
        simpleExecutor.execute(command);
    }
}
