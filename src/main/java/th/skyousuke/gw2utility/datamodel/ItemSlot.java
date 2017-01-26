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

package th.skyousuke.gw2utility.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ItemSlot implements ItemContainer {

    private SimpleObjectProperty<Item> item;
    private SimpleIntegerProperty itemCount;

    public ItemSlot(int itemId, int count) {
        this.item = new SimpleObjectProperty<>(ItemData.getInstance().getItem(itemId));
        this.itemCount = new SimpleIntegerProperty(count);
    }

    public Item getItem() {
        return item.get();
    }

    public SimpleObjectProperty<Item> itemProperty() {
        return item;
    }

    public void setItem(Item item) {
        this.item.set(item);
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public SimpleIntegerProperty itemCountProperty() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount.set(itemCount);
    }

    @Override
    public String toString() {
        return "ItemSlot{" +
                "item=" + item +
                ", itemCount=" + itemCount +
                '}';
    }
}
