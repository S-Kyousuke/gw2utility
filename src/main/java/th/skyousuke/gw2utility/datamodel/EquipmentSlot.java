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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import th.skyousuke.gw2utility.datamodel.property.ItemProperty;

public class EquipmentSlot implements ItemContainer {
    private SimpleObjectProperty<Item> item;
    private SimpleStringProperty slotType;

    public EquipmentSlot(int id, String slotType) {
        this.item = new ItemProperty(ItemData.getInstance().getItem(id));
        this.slotType = new SimpleStringProperty(slotType);
    }

    @Override
    public Item getItem() {
        return item.get();
    }

    public SimpleObjectProperty<Item> itemProperty() {
        return item;
    }

    public void setItem(Item item) {
        this.item.set(item);
    }

    public String getSlotType() {
        return slotType.get();
    }

    public SimpleStringProperty slotTypeProperty() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType.set(slotType);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public String toString() {
        return "EquipmentSlot{" +
                "item=" + item +
                ", slotType=" + slotType +
                '}';
    }
}
