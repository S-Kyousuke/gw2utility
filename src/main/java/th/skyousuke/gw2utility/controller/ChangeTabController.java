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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import th.skyousuke.gw2utility.Main;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.util.AccountDataAutoUpdater;
import th.skyousuke.gw2utility.util.CustomColor;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.util.task.AccountDataTaskRunner;
import th.skyousuke.gw2utility.util.task.UpdateChangeTask;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.WalletNameTableCell;
import th.skyousuke.gw2utility.view.WalletValueTableCell;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;

public class ChangeTabController {

    @FXML
    private TableView<ItemSlot> itemChangeTableView;
    @FXML
    private TableView<Wallet> walletChangeTableView;
    @FXML
    private Button referenceDataButton;
    @FXML
    private Label referenceDataLabel;
    @FXML
    private GridPane mainPane;

    private ObservableList<ItemSlot> itemChange;

    private ObservableList<Wallet> walletChange;

    public void initialize() {
        AccountData.getInstance().setReferenceDataLabel(referenceDataLabel);
        AccountDataAutoUpdater.getInstance().setReferenceDataButton(referenceDataButton);

        walletChange = FXCollections.observableArrayList(AccountData.getInstance().getWalletChange());
        itemChange = FXCollections.observableArrayList(AccountData.getInstance().getItemChange());

        AccountData.getInstance().getItemChange().addListener((ListChangeListener<ItemSlot>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    itemChange.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    itemChange.removeAll(c.getRemoved());
                }
            }
            JavaFXControlUtils.performColumnSort(itemChangeTableView, 0, TableColumn.SortType.DESCENDING);
            JavaFXControlUtils.fixTableViewColumnWidth(itemChangeTableView);
        });
        AccountData.getInstance().getWalletChange().addListener((ListChangeListener<Wallet>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    walletChange.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    walletChange.removeAll(c.getRemoved());
                }
            }
            JavaFXControlUtils.performColumnSort(walletChangeTableView, 1, TableColumn.SortType.DESCENDING);
            JavaFXControlUtils.fixTableViewColumnWidth(walletChangeTableView);
        });

        initItemChangePane();
        initWalletChangePane();
    }

    @SuppressWarnings("unchecked")
    private void initItemChangePane() {
        itemChangeTableView.setItems(itemChange);

        TableColumn<ItemSlot, Integer> countColumn = (TableColumn<ItemSlot, Integer>) itemChangeTableView.getColumns().get(0);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        countColumn.setCellFactory(param -> {
            final ItemCountTableCell<ItemSlot> itemCountTableCell1 = new ItemCountTableCell<>();
            itemCountTableCell1.setSignedDisplay(true);
            itemCountTableCell1.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return itemCountTableCell1;
        });

        TableColumn<ItemSlot, Item> nameColumn = (TableColumn<ItemSlot, Item>) itemChangeTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));
    }

    @SuppressWarnings("unchecked")
    private void initWalletChangePane() {
        walletChangeTableView.setItems(walletChange);

        TableColumn<Wallet, Currency> nameColumn = (TableColumn<Wallet, Currency>) walletChangeTableView.getColumns().get(0);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        nameColumn.setCellFactory(param -> new WalletNameTableCell());
        nameColumn.setComparator(Comparator.comparing(Currency::getName));

        TableColumn<Wallet, Wallet> valueColumn = (TableColumn<Wallet, Wallet>) walletChangeTableView.getColumns().get(1);
        valueColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));
        valueColumn.setCellFactory(param -> {
            final WalletValueTableCell walletValueTableCell = new WalletValueTableCell();
            walletValueTableCell.setShowPlusSign(true);
            walletValueTableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return walletValueTableCell;
        });
        valueColumn.setComparator(Comparator.comparing(Wallet::getValue));
    }

    @FXML
    public void handleSetReferenceDataButton() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Set Reference Data");
        dialog.setHeaderText("Which data do you want to set to reference data?");

        ButtonType saveFileButtonType = new ButtonType("Save File...", ButtonBar.ButtonData.OK_DONE);
        ButtonType currentDataButtonType = new ButtonType("Current Data", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().add(saveFileButtonType);
        dialog.getDialogPane().getButtonTypes().add(currentDataButtonType);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button saveFileButton = (Button) dialog.getDialogPane().lookupButton(saveFileButtonType);
        saveFileButton.setDefaultButton(false);
        saveFileButton.addEventFilter(ActionEvent.ACTION, event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Save File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON File", "*.json")
            );
            File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
            if (selectedFile != null) {
                boolean successful = AccountData.getInstance().setReferenceData(selectedFile.getAbsolutePath());
                if (successful) {
                    AccountDataTaskRunner.getInstance().startTask(UpdateChangeTask.getInstance());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(mainPane.getScene().getWindow());
                    alert.setTitle(Main.APP_NAME);
                    alert.setHeaderText("Set Reference Data Error!");
                    alert.setContentText("Couldn't load data from save file.");
                    alert.showAndWait();
                }
                dialog.close();
            }
            event.consume();
        });
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            ButtonType resultButtonType = result.get();
            if (resultButtonType == currentDataButtonType) {
                AccountData.getInstance().setReferenceData();
                AccountDataTaskRunner.getInstance().startTask(UpdateChangeTask.getInstance());
            }
        }
    }
}
