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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

public class ItemValue {

    private static final BigDecimal LISTING_FEE = new BigDecimal("0.05");
    private static final BigDecimal EXCHANGE_FEE = new BigDecimal("0.10");

    private SimpleObjectProperty<Item> item = new SimpleObjectProperty<>();
    private SimpleIntegerProperty value = new SimpleIntegerProperty();
    private SimpleIntegerProperty incomeValue = new SimpleIntegerProperty();
    private SimpleBooleanProperty tradingPostValue = new SimpleBooleanProperty();

    public ItemValue(Item item, int value, boolean tradingPostValue) {
        this.item.set(item);
        this.incomeValue.bind(Bindings.createIntegerBinding(() -> {
            final int itemValue = this.value.get();

            if (!this.tradingPostValue.get())
                return itemValue;

            final BigDecimal precisionValue = new BigDecimal(itemValue);
            final BigDecimal precisionListingFee = precisionValue.multiply(LISTING_FEE);
            final BigDecimal precisionExchangeFee = precisionValue.multiply(EXCHANGE_FEE);
            final int listingFee = precisionListingFee.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            final int exchangeFee = precisionExchangeFee.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            return itemValue - listingFee - exchangeFee;

        }, this.value, this.tradingPostValue));

        this.value.set(value);
        this.tradingPostValue.set(tradingPostValue);
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

    public int getValue() {
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public int getIncomeValue() {
        return incomeValue.get();
    }

    public SimpleIntegerProperty incomeValueProperty() {
        return incomeValue;
    }

    public boolean isTradingPostValue() {
        return tradingPostValue.get();
    }

    public SimpleBooleanProperty tradingPostValueProperty() {
        return tradingPostValue;
    }

    public void setTradingPostValue(boolean tradingPostValue) {
        this.tradingPostValue.set(tradingPostValue);
    }
}
