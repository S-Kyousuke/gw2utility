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

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.datamodel.WalletValue;
import th.skyousuke.gw2utility.util.CustomColor;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.CoinView;
import th.skyousuke.gw2utility.view.CurrencyValueFactory;
import th.skyousuke.gw2utility.view.PriceInCoinTableCell;
import th.skyousuke.gw2utility.view.WalletNameTableCell;
import th.skyousuke.gw2utility.view.WalletValueTableCell;

import java.util.Comparator;

public class WalletValueChangeController {

    @FXML
    private TableView<WalletValue> walletValueTable;
    @FXML
    private CoinView totalIncomeValue;

    private ObservableList<WalletValue> walletValueChange = FXCollections.observableArrayList();

    public void initialize() {

        // set up data
        totalIncomeValue.setValue(AccountData.getInstance().getWalletValueChangeNumber());
        totalIncomeValue.valueProperty().bind(AccountData.getInstance().walletValueChangeNumberProperty());

        walletValueChange.clear();
        walletValueChange.addAll(AccountData.getInstance().getWalletValueChange());

        // setup data change listener
        final ListChangeListener<WalletValue> walletValueListener = c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    walletValueChange.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    walletValueChange.removeAll(c.getRemoved());
                }
            }
            JavaFXControlUtils.performColumnSort(walletValueTable, 1, TableColumn.SortType.DESCENDING);
            JavaFXControlUtils.fixTableViewColumnWidth(walletValueTable);
        };
        AccountData.getInstance().getWalletValueChange().addListener(walletValueListener);
        walletValueTable.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.setOnCloseRequest(event -> AccountData.getInstance()
                                .getWalletValueChange().removeListener(walletValueListener));
                    }
                });
            }
        });

        // set up table
        initWalletValueTable();
    }

    @SuppressWarnings("unchecked")
    private void initWalletValueTable() {
        walletValueTable.setItems(walletValueChange);

        TableColumn<WalletValue, Currency> nameColumn = (TableColumn<WalletValue, Currency>) walletValueTable.getColumns().get(0);
        nameColumn.setCellValueFactory(new CurrencyValueFactory<>());
        nameColumn.setCellFactory(param -> new WalletNameTableCell<>());

        TableColumn<WalletValue, Wallet> valueColumn = (TableColumn<WalletValue, Wallet>) walletValueTable.getColumns().get(1);
        valueColumn.setCellValueFactory(param -> param.getValue().walletProperty());
        valueColumn.setCellFactory(param -> new WalletValueTableCell<>());
        valueColumn.setComparator(Comparator.comparing(Wallet::getValue));

        TableColumn<WalletValue, Integer> incomeValueColumn = (TableColumn<WalletValue, Integer>) walletValueTable.getColumns().get(2);
        incomeValueColumn.setCellValueFactory(new PropertyValueFactory<>("incomeValue"));
        incomeValueColumn.setCellFactory(param -> {
            final PriceInCoinTableCell<WalletValue> tableCell = new PriceInCoinTableCell<>();
            tableCell.setShowPlusSign(true);
            tableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return tableCell;
        });
        JavaFXControlUtils.performColumnSort(walletValueTable, 1, TableColumn.SortType.DESCENDING);
    }

}
