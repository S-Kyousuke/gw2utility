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

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.util.Gw2Api;
import th.skyousuke.gw2utility.util.Settings;
import th.skyousuke.gw2utility.util.SettingsData;
import th.skyousuke.gw2utility.util.task.AccountDataTaskRunner;
import th.skyousuke.gw2utility.util.task.UpdateAccountTask;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class StartPageController {

    @FXML
    private Button loginButton;
    @FXML
    private Label promptLabel;
    @FXML
    private TextField apiKeyField;
    @FXML
    private CheckBox rememberCheckbox;

    public void initialize() {
        promptLabel.managedProperty().bind(promptLabel.visibleProperty());

        // setup text field listener
        apiKeyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("".equals(newValue)) {
                Platform.runLater(() -> loginButton.setDisable(true));
            } else {
                loginButton.setDisable(false);
            }
        });

        // apply login setting
        Platform.runLater(() -> {
            Settings settings = SettingsData.getInstance().getSettings();
            rememberCheckbox.setSelected(settings.isRememberApiKey());
            apiKeyField.setText(settings.getApiKey());
            loginButton.requestFocus();
        });
    }

    public void handleLoginButton() {
        loginButton.setDisable(true);
        hidePromptLabel();
        AccountData.getInstance().setApiKey(apiKeyField.getText());
        AccountDataTaskRunner.getInstance().startTask(UpdateAccountTask.getInstance());
        new Thread(() -> {
            AccountDataTaskRunner.getInstance().awaitTask(UpdateAccountTask.getInstance());
            if (AccountData.getInstance().getAccount() != null) {
                if (rememberCheckbox.isSelected()) {
                    SettingsData.getInstance().getSettings().setApiKey(apiKeyField.getText());
                    SettingsData.getInstance().saveSettings();
                }
                enterMainPage();
            } else {
                loginButton.setDisable(false);
                if (!Gw2Api.getInstance().isOffline()) {
                    setPromptLabelText("Incorrect API key or API server offline.\nPlease try again.");
                    showPromptLabel();
                } else {
                    setPromptLabelText("Couldn't connect to GW2 API Server.");
                    showPromptLabel();
                }
            }
        }).start();
    }


    private void showPromptLabel() {
        Platform.runLater(() -> {
            promptLabel.setVisible(true);
            promptLabel.getScene().getWindow().sizeToScene();
        });
    }

    private void hidePromptLabel() {
        Platform.runLater(() -> {
            promptLabel.setVisible(false);
            promptLabel.getScene().getWindow().sizeToScene();
        });
    }

    private void enterMainPage() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/mainPage.fxml"));
            } catch (IOException e) {
                Log.error("Exception while loading to main page", e);
                setPromptLabelText("Program fatal error!");
                showPromptLabel();
                loginButton.setDisable(true);
            }
            if (root != null) {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.sizeToScene();
                stage.show();
                stage.centerOnScreen();
            }
        });
    }

    private void setPromptLabelText(String text) {
        Platform.runLater(() -> promptLabel.setText(text));
    }

    @FXML
    public void handleRememberCheckBox() {
        SettingsData.getInstance().getSettings().setRememberApiKey(rememberCheckbox.isSelected());
    }

    @FXML
    public void handleLinkClick() {
        try {
            Desktop.getDesktop().browse(new URI("https://account.arena.net/applications/create"));
        } catch (Exception e) {
            Log.warn("Exception while launching browser", e);
        }
    }

}
