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

package th.skyousuke.gw2utility.util;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class JavaFXControlUtils {

    private JavaFXControlUtils() {
    }

    public static <S> void performColumnSort(TableView<S> tableView, int columnIndex, TableColumn.SortType sortType) {
        TableColumn<S, ?> tableColumn = tableView.getColumns().get(columnIndex);
        Platform.runLater(() -> {
            final ObservableList<TableColumn<S, ?>> sortOrder = tableView.getSortOrder();
            if (!sortOrder.isEmpty()) {
                sortOrder.clear();
            }
            sortOrder.add(tableColumn);
            tableColumn.setSortType(sortType);
            tableColumn.setSortable(true);
        });
    }

    public static void fixTableViewColumnWidth(TableView tableView) {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Platform.runLater(tableView::requestLayout);
        }).start();
    }

}
