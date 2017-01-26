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
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class UpdateCharactersTask implements AccountDataTask {

    private static final UpdateCharactersTask instance = new UpdateCharactersTask();

    private UpdateCharactersTask() {
    }

    public static UpdateCharactersTask getInstance() {
        return instance;
    }

    @Override
    public void runTask(CountDownLatch finishedSignal) {
        AccountDataTaskRunner.getInstance().awaitTask(UpdateCharacterNamesTask.getInstance());
        final List<String> characterNames = AccountData.getInstance().getCharacterNames();

        final String apiKey = AccountData.getInstance().getApiKey();
        if (!characterNames.isEmpty()) {
            final Map<String, Character> downloadedCharacters = Gw2Api.getInstance().getCharacters(characterNames, apiKey);

            final Map<String, Character> characters = AccountData.getInstance().getCharacters();
            if (downloadedCharacters != null) {
                characters.clear();
                characters.putAll(downloadedCharacters);
            }
        }
        finishedSignal.countDown();
    }
}
