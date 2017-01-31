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
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UpdateWalletsTask implements AwaitableTask {

    private static final UpdateWalletsTask instance = new UpdateWalletsTask();

    private UpdateWalletsTask() {
    }

    public static UpdateWalletsTask getInstance() {
        return instance;
    }

    @Override
    public void runTask(CountDownLatch finishedSignal) {
        final String apiKey = AccountData.getInstance().getApiKey();
        final List<Wallet> wallets = AccountData.getInstance().getWallets();
        final List<Wallet> downloadedWallets = Gw2Api.getInstance().getWallets(apiKey);
        if (downloadedWallets != null) {
            wallets.clear();
            final List<Integer> addedCurrency = new ArrayList<>();
            for (Wallet wallet : downloadedWallets) {
                int id = wallet.getCurrency().getId();
                addedCurrency.add(id);
                wallets.add(wallet);
            }
            for (int currencyId : Gw2Api.getInstance().getCurrencyIdSet()) {
                if (!addedCurrency.contains(currencyId)) {
                    wallets.add(new Wallet(currencyId, 0));
                }
            }
        }
        finishedSignal.countDown();
    }
}
