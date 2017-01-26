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

import th.skyousuke.gw2utility.util.gson.GsonHelper;

public class SettingsData {

    private static final String SETTINGS_FILE = "settings.json";

    private static final SettingsData instance = new SettingsData();

    private Settings settings;

    private SettingsData() {
    }

    public static SettingsData getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() {
        GsonHelper.writeJsonToFile(settings, SETTINGS_FILE);
    }

    public void loadSettings() {
        settings = GsonHelper.readJsonFromFile(SETTINGS_FILE, Settings.class);
        if (settings == null) {
            settings = new Settings();
        }
    }
}
