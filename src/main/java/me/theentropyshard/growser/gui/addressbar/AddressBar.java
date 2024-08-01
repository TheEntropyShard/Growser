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

package me.theentropyshard.growser.gui.addressbar;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddressBar extends JPanel {
    private final JButton backButton;
    private final JButton forwardButton;
    private final JButton refreshButton;
    private final JTextField addressField;
    private final JButton moreButton;

    public AddressBar() {
        super(new GridBagLayout());

        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.backButton = new AddressBarButton("/assets/arrow_back_x24.png");
        this.add(this.backButton);

        this.forwardButton = new AddressBarButton("/assets/arrow_forward_x24.png");
        this.add(this.forwardButton);

        this.refreshButton = new AddressBarButton("/assets/refresh_x24.png");
        this.add(this.refreshButton);

        this.addressField = new JTextField();
        this.addressField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                AddressBar.this.addressField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                AddressBar.this.addressField.select(0, 0);
            }
        });
        this.addressField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter URL");
        this.add(this.addressField, gbc);

        this.moreButton = new AddressBarButton("/assets/more_vert_x24.png");
        this.add(this.moreButton);
    }

    public String getUrl() {
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
