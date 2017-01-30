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

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.util.CacheService;


public class ItemNameTableCell<T> extends TableCell<T, Item> {

    private static final int ICON_SIZE = 16;

    private ImageView icon;

    public ItemNameTableCell() {
        setPadding(new Insets(0, 5, 0, 0));
        icon = new ImageView();
        icon.setFitHeight(ICON_SIZE);
        icon.setFitWidth(ICON_SIZE);
        setGraphic(icon);
        setGraphicTextGap(5);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        if (empty || item == null) {
            setText(null);
            icon.setImage(null);
        } else {
            setText(item.getName());
            setTextFill(item.getRarity().getNameColor());
            icon.setImage(CacheService.getImage("file:" + item.getIconPath()));
        }
    }


}
