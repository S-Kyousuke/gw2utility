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

package th.skyousuke.gw2utility.controller;

import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Character;

import java.util.ArrayList;
import java.util.List;

public class DebugPageController {

    @FXML
    private Label dataLabel;

    public void initialize() {
        AccountData.getInstance().getCharacters().addListener(new MapChangeListener<String, Character>() {
            private StringBuilder sb = new StringBuilder();

            @Override
            public void onChanged(Change<? extends String, ? extends Character> change) {
                sb.setLength(0);
                List<Character> characters = new ArrayList<>(AccountData.getInstance().getCharacters().values());
                for (Character character : characters) {
                    sb.append(character.toString());
                    sb.append('\n');
                    sb.append("=================");
                    sb.append('\n');
                }
                Platform.runLater(() -> dataLabel.setText(sb.toString()));
            }
        });

    }

}
