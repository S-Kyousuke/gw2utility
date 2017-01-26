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
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UpdateBankTask implements AccountDataTask {

    private static final UpdateBankTask instance = new UpdateBankTask();

    private UpdateBankTask() {
    }

    public static UpdateBankTask getInstance() {
        return instance;
    }

    @Override
    public void runTask(CountDownLatch finishedSignal) {
        final String apiKey = AccountData.getInstance().getApiKey();
        final List<ItemSlot> bankItemSlots = Gw2Api.getInstance().getBankItemSlots(apiKey);
        final List<ItemSlot> bank = AccountData.getInstance().getBank();
        if (!bankItemSlots.isEmpty()) {
            bank.clear();
            bank.addAll(bankItemSlots);
        }
        finishedSignal.countDown();
    }

}
