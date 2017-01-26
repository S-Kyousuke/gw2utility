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
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionListing {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final SimpleObjectProperty<Transaction> transaction;
    private final SimpleIntegerProperty buyPrice = new SimpleIntegerProperty();
    private final SimpleIntegerProperty sellPrice = new SimpleIntegerProperty();
    private final SimpleLongProperty ageInSeconds = new SimpleLongProperty();

    public TransactionListing(Transaction transaction) {
        this.transaction = new SimpleObjectProperty<>(transaction);

        executor.scheduleAtFixedRate(() -> ageInSeconds.set(getTransaction().getDateCreated()
                .until(LocalDateTime.now(), ChronoUnit.SECONDS)), 0, 1, TimeUnit.SECONDS);
    }

    public void updateSellPrice() {
        new Thread(() -> sellPrice.set(Gw2Api.getInstance().getItemSellPrice(transaction.get().getItemId()))).start();
    }

    public void updateBuyPrice() {
        new Thread(() -> buyPrice.set(Gw2Api.getInstance().getItemBuyPrice(transaction.get().getItemId()))).start();
    }

    public Transaction getTransaction() {
        return transaction.get();
    }

    public SimpleObjectProperty<Transaction> transactionProperty() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction.set(transaction);
    }

    public int getBuyPrice() {
        return buyPrice.get();
    }

    public SimpleIntegerProperty buyPriceProperty() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice.set(buyPrice);
    }

    public int getSellPrice() {
        return sellPrice.get();
    }

    public SimpleIntegerProperty sellPriceProperty() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice.set(sellPrice);
    }

    public long getAgeInSeconds() {
        return ageInSeconds.get();
    }

    public SimpleLongProperty ageInSecondsProperty() {
        return ageInSeconds;
    }

    public void setAgeInSeconds(long ageInSeconds) {
        this.ageInSeconds.set(ageInSeconds);
    }

    public void stopAgeCalculation() {
        executor.shutdown();
    }
}
