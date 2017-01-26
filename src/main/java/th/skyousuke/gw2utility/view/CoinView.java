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

package th.skyousuke.gw2utility.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class CoinView extends HBox {
    @FXML
    private Label goldLabel;
    @FXML
    private Label silverLabel;
    @FXML
    private Label copperLabel;

    private boolean showPlusSign;

    public CoinView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/coinView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        goldLabel.managedProperty().bind(goldLabel.visibleProperty());
        silverLabel.managedProperty().bind(silverLabel.visibleProperty());
        copperLabel.managedProperty().bind(copperLabel.visibleProperty());
    }

    public void setValue(int value) {
        final int absValue = Math.abs(value);
        final int gold = absValue / (10000);
        final int silver = (absValue % (10000)) / 100;
        final int copper = absValue % 100;

        final String valueSign;
        if (showPlusSign) {
            valueSign = value < 0 ? "-" : "+";
        } else {
            valueSign = value < 0 ? "-" : "";
        }

        String goldSign = "";
        String silverSign = "";
        String copperSign =  "";

        if (gold > 0) {
            goldSign = valueSign;
        } else if (silver > 0) {
            silverSign = valueSign;
        } else if (copper > 0) {
            copperSign = valueSign;
        }

        if (gold != 0) {
            goldLabel.setVisible(true);
            goldLabel.setText(goldSign + String.valueOf(gold));
        } else {
            goldLabel.setVisible(false);
        }
        if (silver != 0) {
            silverLabel.setVisible(true);
            silverLabel.setText(silverSign + String.valueOf(silver));
        } else {
            silverLabel.setVisible(false);
        }
        if (copper != 0) {
            copperLabel.setVisible(true);
            copperLabel.setText(copperSign + String.valueOf(copper));
        } else {
            copperLabel.setVisible(false);
        }
    }

    public void setShowPlusSign(boolean showPlusSign) {
        this.showPlusSign = showPlusSign;
    }

    public void setTextFill(Color color) {
        goldLabel.setTextFill(color);
        silverLabel.setTextFill(color);
        copperLabel.setTextFill(color);
    }

}
