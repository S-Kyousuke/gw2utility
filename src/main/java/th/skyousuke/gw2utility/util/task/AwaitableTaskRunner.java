/*
 * Copyright 2017 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.gw2utility.util.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AwaitableTaskRunner {

    private static final AwaitableTaskRunner instance = new AwaitableTaskRunner();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private Map<AwaitableTask, CountDownLatch> finishedSignals = new HashMap<>();

    private AwaitableTaskRunner() {
    }

    public static AwaitableTaskRunner getInstance() {
        return instance;
    }

    public void startTask(AwaitableTask task) {
        final CountDownLatch finishedSignal = new CountDownLatch(1);
        finishedSignals.put(task, finishedSignal);
        executor.execute(() -> task.runTask(finishedSignal));
    }

    public void awaitTask(AwaitableTask task) {
        CountDownLatch updateSignal = finishedSignals.get(task);
        if (updateSignal != null) {
            try {
                updateSignal.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopUpdateService() {
        executor.shutdownNow();
    }

}
