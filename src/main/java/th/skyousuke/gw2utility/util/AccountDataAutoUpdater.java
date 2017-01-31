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

package th.skyousuke.gw2utility.util;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.util.task.AwaitableTaskRunner;
import th.skyousuke.gw2utility.util.task.UpdateBankTask;
import th.skyousuke.gw2utility.util.task.UpdateBuyListTask;
import th.skyousuke.gw2utility.util.task.UpdateChangeTask;
import th.skyousuke.gw2utility.util.task.UpdateCharacterNamesTask;
import th.skyousuke.gw2utility.util.task.UpdateCharactersTask;
import th.skyousuke.gw2utility.util.task.UpdateMaterialTask;
import th.skyousuke.gw2utility.util.task.UpdateSellListTask;
import th.skyousuke.gw2utility.util.task.UpdateWalletsTask;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountDataAutoUpdater {

    private static final int UPDATE_INTERVAL_SECOND = 180;
    private static final int SECOND_TO_MILLISECOND = 1000;

    private static final AccountDataAutoUpdater instance = new AccountDataAutoUpdater();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Timer timer;
    private Timer updateStatusTimer;

    private String timerText = "";
    private String statusText = "";
    private Label statusLabel;
    private Button updateButton;
    private Button saveButton;
    private Button referenceDataButton;

    private AccountDataAutoUpdater() {
    }

    public static AccountDataAutoUpdater getInstance() {
        return instance;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public void setReferenceDataButton(Button referenceDataButton) {
        this.referenceDataButton = referenceDataButton;
    }

    public void startAutoUpdate() {
        timer = new Timer();
        updateStatusTimer = new Timer();
        final TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                updateAll();
            }
        };
        timer.scheduleAtFixedRate(updateTask, 0, (long) UPDATE_INTERVAL_SECOND * SECOND_TO_MILLISECOND);
        updateStatusTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final long currentTime = System.currentTimeMillis();
                final long recentExecutionTime = updateTask.scheduledExecutionTime();
                final int taskDelaySecond = Math.toIntExact((currentTime - recentExecutionTime) / SECOND_TO_MILLISECOND);
                int countdownSecond = UPDATE_INTERVAL_SECOND - taskDelaySecond;
                if (countdownSecond < 0)
                    countdownSecond = 0;
                setTimerText("Next update: " + LocalTime.ofSecondOfDay(countdownSecond).format(DateTimeHelper.timeFormatter));
            }
        }, 0, 1000);
    }

    public void restartAutoUpdate() {
        stopAutoUpdate();
        startAutoUpdate();
    }

    private void stopAutoUpdate() {
        if (timer != null) {
            timer.cancel();
        }
        if (updateStatusTimer != null) {
            updateStatusTimer.cancel();
        }
    }

    private void updateAll() {
        Platform.runLater(() -> {
            saveButton.setDisable(true);
            updateButton.setDisable(true);
            referenceDataButton.setDisable(true);
        });

        final AwaitableTaskRunner awaitableTaskRunner = AwaitableTaskRunner.getInstance();
        awaitableTaskRunner.startTask(UpdateCharacterNamesTask.getInstance());
        awaitableTaskRunner.startTask(UpdateCharactersTask.getInstance());
        awaitableTaskRunner.startTask(UpdateBankTask.getInstance());
        awaitableTaskRunner.startTask(UpdateMaterialTask.getInstance());
        awaitableTaskRunner.startTask(UpdateWalletsTask.getInstance());
        awaitableTaskRunner.startTask(UpdateSellListTask.getInstance());
        awaitableTaskRunner.startTask(UpdateBuyListTask.getInstance());
        awaitableTaskRunner.startTask(UpdateChangeTask.getInstance());

        executor.submit(() -> {
            setStatusText("Updating character name data...");
            awaitableTaskRunner.awaitTask(UpdateCharacterNamesTask.getInstance());
            setStatusText("Updating character data...");
            awaitableTaskRunner.awaitTask(UpdateCharactersTask.getInstance());
            setStatusText("Updating bank data...");
            awaitableTaskRunner.awaitTask(UpdateBankTask.getInstance());
            setStatusText("Updating material data...");
            awaitableTaskRunner.awaitTask(UpdateMaterialTask.getInstance());
            setStatusText("Updating wallet data...");
            awaitableTaskRunner.awaitTask(UpdateWalletsTask.getInstance());
            setStatusText("Updating sell list data...");
            awaitableTaskRunner.awaitTask(UpdateSellListTask.getInstance());
            setStatusText("Updating buy list data...");
            awaitableTaskRunner.awaitTask(UpdateBuyListTask.getInstance());

            setStatusText("Updating change data...");
            if (AccountData.getInstance().isReferenceDataSet()) {
                awaitableTaskRunner.awaitTask(UpdateChangeTask.getInstance());
            } else {
                AccountData.getInstance().setReferenceData();
                awaitableTaskRunner.awaitTask(UpdateChangeTask.getInstance());
            }
            setStatusText("Update Completed: " + DateTimeHelper.dateTimeFormatter.format(LocalDateTime.now()));

            Platform.runLater(() -> {
                saveButton.setDisable(false);
                updateButton.setDisable(false);
                referenceDataButton.setDisable(false);
            });
        });
    }

    private void updateStatusLabel() {
        if (statusLabel != null)
            Platform.runLater(() -> statusLabel.setText(timerText + " - " + statusText));
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void stopUpdateService() {
        stopAutoUpdate();
        executor.shutdownNow();
    }

    private void setTimerText(String timerText) {
        this.timerText = timerText;
        updateStatusLabel();
    }

    private void setStatusText(String statusText) {
        this.statusText = statusText;
        updateStatusLabel();
    }
}
