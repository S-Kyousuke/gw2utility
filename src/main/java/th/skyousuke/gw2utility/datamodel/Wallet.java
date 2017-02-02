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

public class Wallet implements CurrencyContainer {
    private SimpleObjectProperty<Currency> currency;
    private SimpleIntegerProperty value;

    public Wallet(int currencyId, int value) {
        currency = new SimpleObjectProperty<>(CurrencyData.getInstance().getCurrency(currencyId));
        this.value = new SimpleIntegerProperty(value);
    }

    @Override
    public Currency getCurrency() {
        return currency.get();
    }

    public SimpleObjectProperty<Currency> currencyProperty() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency.set(currency);
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

    @Override
    public String toString() {
        return "Wallet{" +
                "currency=" + currency +
                ", value=" + value +
                '}';
    }
}
