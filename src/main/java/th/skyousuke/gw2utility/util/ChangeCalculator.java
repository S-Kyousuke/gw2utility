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

import com.esotericsoftware.minlog.Log;
import th.skyousuke.gw2utility.datamodel.ItemContainer;
import th.skyousuke.gw2utility.datamodel.ItemContainerValue;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.ItemValue;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.datamodel.WalletValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChangeCalculator {

    private Map<Integer, Integer> itemChangeMap = new HashMap<>();
    private Map<Integer, Integer> walletChangeMap = new HashMap<>();

    private ItemValueCalculator itemValueCalculator = new ItemValueCalculator();
    private WalletValueCalculator walletValueCalculator = new WalletValueCalculator();

    public void startCalculateItemChange() {
        itemChangeMap.clear();
    }

    public void startCalculateWalletChange() {
        walletChangeMap.clear();
    }

    public void stopItemChange() {
        itemChangeMap.entrySet().removeIf(entry -> entry.getValue() == 0);
    }

    public void stopWalletChange() {
        walletChangeMap.entrySet().removeIf(entry -> entry.getValue() == 0);
    }

    public void addCurrentItem(ItemContainer currentItem) {
        final int itemId = currentItem.getItem().getId();
        final int itemCount = currentItem.getItemCount();
        itemChangeMap.put(itemId, itemChangeMap.getOrDefault(itemId, 0) + itemCount);
    }

    public void addCurrentItems(List<? extends ItemContainer> currentItems) {
        for (ItemContainer itemContainer : currentItems) {
            addCurrentItem(itemContainer);
        }
    }

    public void addPreviousItem(ItemContainer previousItem) {
        final int itemId = previousItem.getItem().getId();
        final int itemCount = previousItem.getItemCount();
        itemChangeMap.put(itemId, itemChangeMap.getOrDefault(itemId, 0) - itemCount);
    }

    public void addPreviousItems(List<? extends ItemContainer> previousItems) {
        for (ItemContainer itemContainer : previousItems) {
            addPreviousItem(itemContainer);
        }
    }

    public void addCurrentWallet(Wallet currentWallet) {
        final int currencyId = currentWallet.getCurrency().getId();
        final int value = currentWallet.getValue();
        walletChangeMap.put(currencyId, walletChangeMap.getOrDefault(currencyId, 0) + value);
    }

    public void addCurrentWallets(List<Wallet> currentWallets) {
        for (Wallet currentWallet : currentWallets) {
            addCurrentWallet(currentWallet);
        }
    }

    public void addPreviousWallet(Wallet previousWallet) {
        final int currencyId = previousWallet.getCurrency().getId();
        final int value = previousWallet.getValue();
        walletChangeMap.put(currencyId, walletChangeMap.getOrDefault(currencyId, 0) - value);
    }

    public void addPreviousWallets(List<Wallet> previousWallets) {
        for (Wallet previousWallet : previousWallets) {
            addPreviousWallet(previousWallet);
        }
    }

    public List<ItemSlot> getItemChange() {
        List<ItemSlot> itemChange = new ArrayList<>();
        itemChangeMap.forEach((itemId, count) -> itemChange.add(new ItemSlot(itemId, count)));
        return itemChange;
    }

    public List<Wallet> getWalletChange() {
        List<Wallet> walletChange = new ArrayList<>();
        walletChangeMap.forEach((itemId, count) -> walletChange.add(new Wallet(itemId, count)));
        return walletChange;
    }

    public List<ItemContainerValue> getItemValueChange(List<ItemSlot> itemChange) {
        ExecutorService service = Executors.newCachedThreadPool();
        List<ItemContainerValue> itemChangeValue = new ArrayList<>();
        for (ItemSlot itemSlot : itemChange) {
            service.submit(() -> {
                ItemValue itemValue = itemValueCalculator.getItemValue(itemSlot.getItem());
                itemChangeValue.add(new ItemContainerValue(itemValue, itemSlot.itemCountProperty()));
            });
        }
        service.shutdown();
        try {
            if (!service.awaitTermination(5, TimeUnit.MINUTES)) {
                Log.warn("Item value calculation failed!");
                return new ArrayList<>();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.warn("Item value calculation is interrupted!");
        }
        itemChangeValue.removeIf(Objects::isNull);
        return itemChangeValue;
    }

    public List<WalletValue> getWalletValueChange(List<Wallet> walletChange) {
        List<WalletValue> walletChangeValue = new ArrayList<>();
        for (Wallet wallet : walletChange) {
            walletChangeValue.add(walletValueCalculator.getWalletValue(wallet));
        }
        return walletChangeValue;
    }
}
