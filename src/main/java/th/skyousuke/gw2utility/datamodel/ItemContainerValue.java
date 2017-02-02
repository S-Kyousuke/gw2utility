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

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;

public class ItemContainerValue {

    private ItemValue itemValue;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty totalIncomeValue = new SimpleIntegerProperty();

    public ItemContainerValue(ItemValue itemValue, SimpleIntegerProperty quantity) {
        this.itemValue = itemValue;
        this.quantity = quantity;

        this.totalIncomeValue.bind(Bindings.multiply(itemValue.incomeValueProperty(), this.quantity));
    }

    public ItemValue getItemValue() {
        return itemValue;
    }

    public void setItemValue(ItemValue itemValue) {
        this.itemValue = itemValue;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getTotalIncomeValue() {
        return totalIncomeValue.get();
    }

    public SimpleIntegerProperty totalIncomeValueProperty() {
        return totalIncomeValue;
    }
}
