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

package th.skyousuke.gw2utility.view;

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import th.skyousuke.gw2utility.datamodel.Wallet;

public class WalletValueTableCell extends TableCell<Wallet, Wallet> {

    private boolean showPlusSign;

    private final CoinView coinView = new CoinView();

    private Color positiveValueColor = Color.WHITE;
    private Color negativeValueColor = Color.WHITE;
    private Color zeroValueColor = Color.WHITE;

    public WalletValueTableCell() {
        setPadding(new Insets(0, 10, 0, 0));
    }

    @Override
    protected void updateItem(Wallet wallet, boolean empty) {
        if (empty || wallet == null) {
            setText(null);
            setGraphic(null);
        } else {
            final int value = wallet.getValue();

            // if the currency is coin type, display value in gold, silver and copper.
            if (wallet.getCurrency().getId() == 1) {
                coinView.setShowPlusSign(showPlusSign);
                coinView.setValue(value);
                if (value > 0) {
                    coinView.setTextFill(positiveValueColor);
                } else if (value < 0) {
                    coinView.setTextFill(negativeValueColor);
                } else {
                    coinView.setTextFill(zeroValueColor);
                }
                setText(null);
                setGraphic(coinView);
            } else {
                if (showPlusSign) {
                    setText((value > 0 ? "+" : "") + value);
                } else {
                    setText(String.valueOf(value));
                }
                if (value > 0) {
                    setWalletValueColor(positiveValueColor);
                } else if (value < 0) {
                    setWalletValueColor(negativeValueColor);
                } else {
                    setWalletValueColor(zeroValueColor);
                }
                setGraphic(null);
            }
        }
    }

    /**
     * Set wallet value text color using CSS
     * @param color value text color
     */
    private void setWalletValueColor(Color color) {
        setStyle("-fx-alignment: center-right; -fx-text-fill: " +
                color.toString().replace("0x", "#") + ';');
    }

    public void setShowPlusSign(boolean showPlusSign) {
        this.showPlusSign = showPlusSign;
    }

    public void setTextColor(Color zeroValueColor, Color positiveValueColor, Color negativeValueColor) {
        this.zeroValueColor = zeroValueColor;
        this.positiveValueColor = positiveValueColor;
        this.negativeValueColor = negativeValueColor;
    }


}
