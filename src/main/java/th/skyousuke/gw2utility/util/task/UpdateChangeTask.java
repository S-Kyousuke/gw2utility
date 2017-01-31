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

package th.skyousuke.gw2utility.util.task;

import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Bag;
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.Transaction;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.util.ChangeCalculator;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.concurrent.CountDownLatch;

public class UpdateChangeTask implements AwaitableTask {

    public static final UpdateChangeTask instance = new UpdateChangeTask();

    private ChangeCalculator changeCalculator = new ChangeCalculator();

    private UpdateChangeTask() {
    }

    public static UpdateChangeTask getInstance() {
        return instance;
    }

    @Override
    public void runTask(CountDownLatch finishedSignal) {
        awaitSynchronizedData();

        updateItemChange(AccountData.getInstance());
        updateWalletChange(AccountData.getInstance());
        updateValueChange(AccountData.getInstance());

        finishedSignal.countDown();
    }

    private void awaitSynchronizedData() {
        AwaitableTaskRunner.getInstance().awaitTask(UpdateCharacterNamesTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateCharactersTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateBankTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateMaterialTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateWalletsTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateSellListTask.getInstance());
        AwaitableTaskRunner.getInstance().awaitTask(UpdateBuyListTask.getInstance());
    }

    private void updateItemChange(AccountData accountData) {
        changeCalculator.startCalculateItemChange();

        // currentItems
        for (Character character : accountData.getCharacters().values()) {
            changeCalculator.addCurrentItems(character.getEquipment().getEquipmentSlots());
            for (Bag bag : character.getBags()) {
                changeCalculator.addCurrentItems(bag.getInventory().getItemSlots());
            }
        }
        changeCalculator.addCurrentItems(accountData.getBank());
        changeCalculator.addCurrentItems(accountData.getMaterial());
        for (Transaction sellTransaction : accountData.getSellTransactions()) {
            changeCalculator.addCurrentItem(new ItemSlot(sellTransaction.getItemId(), sellTransaction.getQuantity()));
        }
        // previousItems
        for (Character character : accountData.getReferenceCharacters().values()) {
            changeCalculator.addPreviousItems(character.getEquipment().getEquipmentSlots());
            for (Bag bag : character.getBags()) {
                changeCalculator.addPreviousItems(bag.getInventory().getItemSlots());
            }
        }
        changeCalculator.addPreviousItems(accountData.getReferenceBank());
        changeCalculator.addPreviousItems(accountData.getReferenceMaterial());
        for (Transaction sellTransaction : accountData.getReferenceSellTransactions()) {
            changeCalculator.addPreviousItem(new ItemSlot(sellTransaction.getItemId(), sellTransaction.getQuantity()));
        }
        // update item change result
        changeCalculator.stopItemChange();
        accountData.getItemChange().clear();
        accountData.getItemChange().addAll(changeCalculator.getItemChange());
    }

    private void updateWalletChange(AccountData accountData) {
        changeCalculator.startCalculateWalletChange();

        // current wallet
        changeCalculator.addCurrentWallets(accountData.getWallets());
        for (Transaction buyTransaction : accountData.getBuyTransactions()) {
            changeCalculator.addCurrentWallet(new Wallet(Gw2Api.COIN_ID, buyTransaction.getPrice() * buyTransaction.getQuantity()));
        }
        // previous wallet
        changeCalculator.addPreviousWallets(accountData.getReferenceWallets());
        for (Transaction buyTransaction : accountData.getReferenceBuyTransactions()) {
            changeCalculator.addPreviousWallet(new Wallet(Gw2Api.COIN_ID, buyTransaction.getPrice() * buyTransaction.getQuantity()));
        }
        // update wallet change result
        changeCalculator.stopWalletChange();
        accountData.getWalletChange().clear();
        accountData.getWalletChange().addAll(changeCalculator.getWalletChange());
    }

    private void updateValueChange(AccountData accountData) {
        accountData.getItemValueChange().clear();
        accountData.getWalletValueChange().clear();

        accountData.getItemValueChange().addAll(changeCalculator.getItemValueChange(accountData.getItemChange()));
        accountData.getWalletValueChange().addAll(changeCalculator.getWalletValueChange(accountData.getWalletChange()));
    }
}
