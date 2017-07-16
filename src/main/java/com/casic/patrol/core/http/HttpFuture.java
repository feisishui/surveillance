package com.casic.patrol.core.http;

import java.io.IOException;
import java.util.concurrent.*;

public class HttpFuture implements Callable<String> {
    private String url;

    public HttpFuture(String url) {
        this.url = url;
    }

    public String call() throws Exception {
        return new HttpHandlerImpl().readText(url);
    }

    public String readText(ExecutorService executorService, long timeout)
            throws IOException, InterruptedException, ExecutionException,
            TimeoutException {
        FutureTask<String> futureTask = new FutureTask(this);
        executorService.execute(futureTask);

        String text = futureTask.get(timeout, TimeUnit.MILLISECONDS);

        return text;
    }
}
