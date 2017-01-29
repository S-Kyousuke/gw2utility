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
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Wallet;

import java.util.Observer;


public class WalletNameTableCell  extends TableCell<Wallet, Currency>  {

    private static final int ICON_SIZE = 16;

    private Observer observer;
    private ImageView icon;

    public WalletNameTableCell() {
        setPadding(new Insets(0, 0, 0, 5));
        icon = new ImageView();
        icon.setFitHeight(ICON_SIZE);
        icon.setFitWidth(ICON_SIZE);
        setGraphic(icon);
        setGraphicTextGap(5);
    }

    @Override
    protected void updateItem(Currency currency, boolean empty) {
        if (currency != null)
            currency.deleteObserver(observer);
        if (empty || currency == null) {
            setText(null);
            icon.setImage(null);
        } else {
            setText(currency.getName());
            icon.setImage(new Image("file:" + currency.getIconPath()));
            observer = (o, arg) -> Platform.runLater(() -> {
                setText(currency.getName());
                icon.setImage(new Image("file:" + currency.getIconPath()));
            });
            currency.addObserver(observer);
        }
    }
}
