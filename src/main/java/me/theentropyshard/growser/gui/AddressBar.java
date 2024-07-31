/*
 * Growser - https://github.com/TheEntropyShard/Growser
 * Copyright (C) 2023-2024 TheEntropyShard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.growser.gui;

import com.formdev.flatlaf.FlatClientProperties;
import me.theentropyshard.growser.utils.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class AddressBar extends JPanel {
    private final JButton backButton;
    private final JButton forwardButton;
    private final JButton refreshButton;
    private final JTextField addressField;
    private final JButton moreButton;

    public AddressBar() {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.backButton = new JButton(SwingUtils.getIcon("/assets/arrow_back_x24.png"));
        this.backButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        this.add(this.backButton);

        this.forwardButton = new JButton(SwingUtils.getIcon("/assets/arrow_forward_x24.png"));
        this.forwardButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        this.add(this.forwardButton);

        this.refreshButton = new JButton(SwingUtils.getIcon("/assets/refresh_x24.png"));
        this.refreshButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        this.add(this.refreshButton);

        this.addressField = new JTextField();
        this.addressField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter URL");
        this.add(this.addressField, gbc);

        this.moreButton = new JButton(SwingUtils.getIcon("/assets/more_vert_x24.png"));
        this.moreButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        this.add(this.moreButton);
    }

    public void getUrl() {
        return this.addressField.getText();
    }

    public void setUrl(String url) {
        this.addressField.setText(url);
    }

    public JButton getBackButton() {
        return this.backButton;
    }

    public JButton getForwardButton() {
        return this.forwardButton;
    }

    public JButton getRefreshButton() {
        return this.refreshButton;
    }

    public JTextField getAddressField() {
        return this.addressField;
    }

    public JButton getMoreButton() {
        return this.moreButton;
    }
}
