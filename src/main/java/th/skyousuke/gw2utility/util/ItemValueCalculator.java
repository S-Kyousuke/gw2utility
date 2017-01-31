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

import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemValue;

public class ItemValueCalculator {

    public ItemValue getItemValue(Item item) {
        try {
            item.waitData();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int tpPrice = 0;
        if (!item.isBoundOnAcquire()) {
            tpPrice = CacheService.getItemSellPrice(item.getId());
        }
        if (tpPrice != 0) {
            return new ItemValue(item, tpPrice, true);
        } else if (!item.isNoSell()) {
            return new ItemValue(item, item.getVendorPrice(), false);
        }
        return new ItemValue(item, 0, false);
    }
}
