/*
 * Growser - https://github.com/TheEntropyShard/Growser
 * Copyright (C) 2023-2025 TheEntropyShard
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

import me.theentropyshard.growser.gui.addressbar.AddressBar;
import me.theentropyshard.growser.utils.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tab extends JPanel {
    public static final String ENTER_ADDRESS_KEY = "ENTER_ADDRESS";
    public static final String SCROLL_TO_TOP_KEY = "SCROLL_TO_TOP_KEY";
    public static final String SCROLL_TO_BOTTOM_KEY = "SCROLL_TO_BOTTOM_KEY";

    private final JTabbedPane tabbedPane;

    private final AddressBar addressBar;
    private final GeminiPanel geminiPanel;

    public Tab(JTabbedPane tabbedPane) {
        this(null, tabbedPane);
    }

    public Tab(String url, JTabbedPane tabbedPane) {
        super(new BorderLayout());

        this.tabbedPane = tabbedPane;

        this.addressBar = new AddressBar();
        this.addressBar.setUrl(url);

        SwingUtils.addKeystroke(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), this.addressBar.getAddressField(),
            JComponent.WHEN_FOCUSED, Tab.ENTER_ADDRESS_KEY,
            SwingUtils.newAction(e -> Tab.this.loadURL(Tab.this.addressBar.getUrl()))
        );

        this.add(this.addressBar, BorderLayout.NORTH);

        this.geminiPanel = new GeminiPanel(tabbedPane, this.addressBar);

        SwingUtils.addKeystroke(
            KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), this.geminiPanel.getTextPane(),
            JComponent.WHEN_FOCUSED, Tab.SCROLL_TO_TOP_KEY,
            SwingUtils.newAction(e -> Tab.this.geminiPanel.scrollToTop())
        );

        SwingUtils.addKeystroke(
            KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), this.geminiPanel.getTextPane(),
            JComponent.WHEN_FOCUSED, Tab.SCROLL_TO_BOTTOM_KEY,
            SwingUtils.newAction(e -> Tab.this.geminiPanel.scrollToBottom())
        );

        this.add(this.geminiPanel, BorderLayout.CENTER);

        if (url != null) {
            this.loadURL(url);
        }
    }

    private void loadURL(String url) {
        String finalUrl;

        if (!url.startsWith("gemini://")) {
            finalUrl = "gemini://" + url;
        } else {
            finalUrl = url;
        }

        new LoadURLWorker(finalUrl, this, e -> System.err.println(
            "Could not load " + finalUrl + " because: " + e.getMessage()
        )).execute();
    }

    public void onClose() {

    }

    public JTabbedPane getTabbedPane() {
        return this.tabbedPane;
    }

    public GeminiPanel getGeminiPanel() {
        return this.geminiPanel;
    }
}
