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

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.CurrencyValueFactory;
import th.skyousuke.gw2utility.view.WalletNameTableCell;
import th.skyousuke.gw2utility.view.WalletValueTableCell;

import java.util.Comparator;

public class WalletPageController {

    @FXML
    private TableView<Wallet> walletTableView;

    public void initialize() {
        AccountData.getInstance().getWallets().addListener((ListChangeListener<Wallet>) c -> JavaFXControlUtils.fixTableViewColumnWidth(walletTableView));
        initWalletPane();
    }

    @SuppressWarnings("unchecked")
    private void initWalletPane() {
        walletTableView.setItems(AccountData.getInstance().getWallets());

        TableColumn<Wallet, Currency> nameColumn = (TableColumn<Wallet, Currency>) walletTableView.getColumns().get(0);
        nameColumn.setCellValueFactory(new CurrencyValueFactory<>());
        nameColumn.setCellFactory(param -> new WalletNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Currency::getName));

        TableColumn<Wallet, Wallet> valueColumn = (TableColumn<Wallet, Wallet>) walletTableView.getColumns().get(1);
        valueColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        valueColumn.setCellFactory(param -> new WalletValueTableCell<>());
        valueColumn.setComparator(Comparator.comparing(Wallet::getValue));
    }

}
