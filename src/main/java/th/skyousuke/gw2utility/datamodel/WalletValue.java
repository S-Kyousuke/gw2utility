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

public class WalletValue implements CurrencyContainer {

    private SimpleObjectProperty<Wallet> wallet = new SimpleObjectProperty<>();
    private SimpleIntegerProperty incomeValue = new SimpleIntegerProperty();

    public WalletValue(Wallet wallet, int incomeValue) {
        this.wallet.set(wallet);
        this.incomeValue.set(incomeValue);
    }

    public Wallet getWallet() {
        return wallet.get();
    }

    public SimpleObjectProperty<Wallet> walletProperty() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet.set(wallet);
    }

    public int getIncomeValue() {
        return incomeValue.get();
    }

    public SimpleIntegerProperty incomeValueProperty() {
        return incomeValue;
    }

    public void setIncomeValue(int incomeValue) {
        this.incomeValue.set(incomeValue);
    }

    @Override
    public Currency getCurrency() {
        return wallet.get().getCurrency();
    }
}
