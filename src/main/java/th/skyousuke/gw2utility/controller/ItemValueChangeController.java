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
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemContainerValue;
import th.skyousuke.gw2utility.util.CustomColor;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.CoinView;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.PriceInCoinTableCell;


public class ItemValueChangeController {

    @FXML
    private TableView<ItemContainerValue> itemValueTable;
    @FXML
    private CoinView totalIncomeValue;

    private ObservableList<ItemContainerValue> itemValueChange = FXCollections.observableArrayList();

    public void initialize() {
        // set up data
        totalIncomeValue.setValue(AccountData.getInstance().getItemValueChangeNumber());
        totalIncomeValue.valueProperty().bind(AccountData.getInstance().itemValueChangeNumberProperty());

        itemValueChange.clear();
        itemValueChange.addAll(AccountData.getInstance().getItemValueChange());

        // setup data change listener
        final ListChangeListener<ItemContainerValue> itemValueListener = c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    itemValueChange.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    itemValueChange.removeAll(c.getRemoved());
                }
            }
            JavaFXControlUtils.performColumnSort(itemValueTable, 0, TableColumn.SortType.DESCENDING);
            JavaFXControlUtils.fixTableViewColumnWidth(itemValueTable);
        };
        AccountData.getInstance().getItemValueChange().addListener(itemValueListener);
        itemValueTable.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.setOnCloseRequest(event -> AccountData.getInstance()
                                .getItemValueChange().removeListener(itemValueListener));
                    }
                });
            }
        });

        // set up table
        initItemValueTable();
    }


    @SuppressWarnings("unchecked")
    private void initItemValueTable() {
        itemValueTable.setItems(itemValueChange);

        TableColumn<ItemContainerValue, Integer> quantityColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(0);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(param -> {
            final ItemCountTableCell<ItemContainerValue> tableCell = new ItemCountTableCell<>();
            tableCell.setShowPlusSign(true);
            tableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return tableCell;
        });

        TableColumn<ItemContainerValue, Item> nameColumn = (TableColumn<ItemContainerValue, Item>) itemValueTable.getColumns().get(1);
        nameColumn.setCellValueFactory(param -> param.getValue().getItemValue().itemProperty());
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());

        TableColumn<ItemContainerValue, Integer> valueColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(2);
        valueColumn.setCellValueFactory(param -> param.getValue().getItemValue().valueProperty().asObject());
        valueColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<ItemContainerValue, Integer> tradableValueColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(3);
        tradableValueColumn.setCellValueFactory(param -> param.getValue().getItemValue().incomeValueProperty().asObject());
        tradableValueColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<ItemContainerValue, Integer> totalTradableValueColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(4);
        totalTradableValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalIncomeValue"));
        totalTradableValueColumn.setCellFactory(param -> {
            final PriceInCoinTableCell<ItemContainerValue> tableCell = new PriceInCoinTableCell<>();
            tableCell.setShowPlusSign(true);
            tableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return tableCell;
        });


        JavaFXControlUtils.performColumnSort(itemValueTable, 0, TableColumn.SortType.DESCENDING);
    }
}
