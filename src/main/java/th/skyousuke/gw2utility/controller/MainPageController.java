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

package th.skyousuke.gw2utility.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import th.skyousuke.gw2utility.Main;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.util.AccountDataAutoUpdater;

import java.io.File;
import java.util.Optional;

public class MainPageController {

    public static final int PAGE_WIDTH = 740;
    public static final int PAGE_HEIGHT = 600;

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label updateStatusLabel;
    @FXML
    private Button updateButton;
    @FXML
    private Button saveButton;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TradingPostPageController tradingPostPageController;
    @FXML
    private Tab debugTab;
    @FXML
    private TabPane mainTabPane;

    @SuppressWarnings("unchecked")
    public void initialize() {
        initExitConfirm();
        welcomeLabel.setText("Welcome, " + AccountData.getInstance().getAccount().getName());

        AccountDataAutoUpdater.getInstance().setStatusLabel(updateStatusLabel);
        AccountDataAutoUpdater.getInstance().setUpdateButton(updateButton);
        AccountDataAutoUpdater.getInstance().setSaveButton(saveButton);
        AccountDataAutoUpdater.getInstance().startAutoUpdate();

        mainTabPane.getTabs().remove(debugTab);
    }

    private void initExitConfirm() {
        mainPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observable1, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.setOnCloseRequest(event -> {
                            event.consume();
                            showExitConfirmDialog();
                        });
                    }
                });
            }
        });
    }

    private void showExitConfirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to exit " + Main.APP_NAME + '?');

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
            tradingPostPageController.dispose();
        }
    }


    @FXML
    public void handleUpdateButton() {
        AccountDataAutoUpdater.getInstance().restartAutoUpdate();
    }

    @FXML
    public void handleSaveDataButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Account Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON file", "*.json")
        );
        File selectedFile = fileChooser.showSaveDialog(mainPane.getScene().getWindow());
        if (selectedFile != null) {
            AccountData.getInstance().exportCurrentData(selectedFile.getAbsolutePath());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainPane.getScene().getWindow());
            alert.setTitle(Main.APP_NAME);
            alert.setHeaderText("");
            alert.setContentText("Account data file has been saved successfully.");
            alert.showAndWait();
        }
    }

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.F8) {
            toggleDebugTab();
        }
    }

    private void toggleDebugTab() {
        final ObservableList<Tab> tabs = mainTabPane.getTabs();
        if (tabs.contains(debugTab)) {
            tabs.remove(debugTab);
        } else {
            tabs.add(debugTab);
        }
    }
}
