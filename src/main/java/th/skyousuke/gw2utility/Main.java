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

package th.skyousuke.gw2utility;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import th.skyousuke.gw2utility.datamodel.CurrencyData;
import th.skyousuke.gw2utility.datamodel.ItemData;
import th.skyousuke.gw2utility.util.AccountDataAutoUpdater;
import th.skyousuke.gw2utility.util.SettingsData;
import th.skyousuke.gw2utility.util.task.AccountDataTaskRunner;

import java.io.IOException;

public class Main extends Application {

    public static final String APP_NAME = "GW2 Utility";
    public static final String VERSION = "1.0.4";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/startPage.fxml"));
        primaryStage.setTitle(APP_NAME + ' ' + VERSION);
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    @Override
    public void init() throws Exception {
        SettingsData.getInstance().loadSettings();
        ItemData.getInstance().loadData();
        CurrencyData.getInstance().loadData();
    }

    @Override
    public void stop() throws Exception {
        AccountDataTaskRunner.getInstance().stopUpdateService();
        AccountDataAutoUpdater.getInstance().stopUpdateService();
        ItemData.getInstance().stopUpdateService();
        CurrencyData.getInstance().stopUpdateService();
        SettingsData.getInstance().saveSettings();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
