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

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import th.skyousuke.gw2utility.util.FatalException;

import java.io.IOException;

public class CoinView extends HBox {

    @FXML
    private Label goldLabel;
    @FXML
    private Label silverLabel;
    @FXML
    private Label copperLabel;

    private SimpleIntegerProperty value = new SimpleIntegerProperty();
    private boolean showPlusSign;

    public CoinView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/coinView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new FatalException(exception);
        }

        goldLabel.managedProperty().bind(goldLabel.visibleProperty());
        silverLabel.managedProperty().bind(silverLabel.visibleProperty());
        copperLabel.managedProperty().bind(copperLabel.visibleProperty());

        value.addListener((observable, oldValue, newValue) -> Platform.runLater(this::updateUi));
        updateUi();
    }

    private void updateUi() {
        final int absValue = Math.abs(value.get());
        final int absGold = absValue / (10000);
        final int absSilver = (absValue % (10000)) / 100;
        final int absCopper = absValue % 100;

        final String valueSign;
        if (showPlusSign) {
            valueSign = value.get() < 0 ? "-" : "+";
        } else {
            valueSign = value.get() < 0 ? "-" : "";
        }

        String goldSign = "";
        String silverSign = "";
        String copperSign = "";

        if (absGold > 0) {
            goldSign = valueSign;
        } else if (absSilver > 0) {
            silverSign = valueSign;
        } else if (absCopper > 0) {
            copperSign = valueSign;
        }

        if (absGold != 0) {
            goldLabel.setText(goldSign + String.valueOf(absGold));
            goldLabel.setVisible(true);
        } else {
            goldLabel.setVisible(false);

        }
        if (absSilver != 0 || absGold != 0) {
            silverLabel.setText(silverSign + String.valueOf(absSilver));
            silverLabel.setVisible(true);
        } else {
            silverLabel.setVisible(false);
        }
        copperLabel.setText(copperSign + String.valueOf(absCopper));
    }

    public void setShowPlusSign(boolean showPlusSign) {
        this.showPlusSign = showPlusSign;
    }

    public void setTextFill(Color color) {
        goldLabel.setTextFill(color);
        silverLabel.setTextFill(color);
        copperLabel.setTextFill(color);
    }

    public int getValue() {
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }
}
