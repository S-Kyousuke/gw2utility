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


import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;

import java.util.Comparator;

public class BankAndMaterialTabController {

    @FXML
    private TableView<ItemSlot> bankTableView;
    @FXML
    private TableView<ItemSlot> materialTableView;

    public void initialize() {
        AccountData.getInstance().getBank().addListener((ListChangeListener<ItemSlot>) c -> JavaFXControlUtils.fixTableViewColumnWidth(bankTableView));
        AccountData.getInstance().getMaterial().addListener((ListChangeListener<ItemSlot>) c -> JavaFXControlUtils.fixTableViewColumnWidth(materialTableView));

        initBankTableView();
        initMaterialTableView();
    }

    @SuppressWarnings("unchecked")
    private void initBankTableView() {
        bankTableView.setItems(AccountData.getInstance().getBank());

        TableColumn<ItemSlot, Integer> countColumn = (TableColumn<ItemSlot, Integer>) bankTableView.getColumns().get(0);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        countColumn.setCellFactory(param -> new ItemCountTableCell<>());

        TableColumn<ItemSlot, Item> nameColumn = (TableColumn<ItemSlot, Item>) bankTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));
    }

    @SuppressWarnings("unchecked")
    private void initMaterialTableView() {
        materialTableView.setItems(AccountData.getInstance().getMaterial());

        TableColumn<ItemSlot, Integer> countColumn = (TableColumn<ItemSlot, Integer>) materialTableView.getColumns().get(0);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        countColumn.setCellFactory(param -> new ItemCountTableCell<>());

        TableColumn<ItemSlot, Item> nameColumn = (TableColumn<ItemSlot, Item>) materialTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));
    }
}
