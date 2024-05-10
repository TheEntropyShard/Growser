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

import me.theentropyshard.growser.gemini.protocol.GeminiFetch;
import me.theentropyshard.growser.gemini.text.GemtextParser;
import me.theentropyshard.growser.gemini.text.HTMLConverter;
import me.theentropyshard.growser.gemini.text.document.GemtextDocument;
import me.theentropyshard.growser.utils.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

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

        SwingUtils.addKeystroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), this.addressBar.getAddressField(),
                JComponent.WHEN_FOCUSED, Tab.ENTER_ADDRESS_KEY,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tab.this.loadURL(Tab.this.addressBar.getAddressField().getText());
                    }
                }
        );

        this.add(this.addressBar, BorderLayout.NORTH);

        this.geminiPanel = new GeminiPanel(tabbedPane);

        SwingUtils.addKeystroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), this.geminiPanel.getTextPane(),
                JComponent.WHEN_FOCUSED, Tab.SCROLL_TO_TOP_KEY,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tab.this.geminiPanel.scrollToTop();
                    }
                }
        );

        SwingUtils.addKeystroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), this.geminiPanel.getTextPane(),
                JComponent.WHEN_FOCUSED, Tab.SCROLL_TO_BOTTOM_KEY,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Tab.this.geminiPanel.scrollToBottom();
                    }
                }
        );

        this.add(this.geminiPanel, BorderLayout.CENTER);

        if (url != null) {
            this.loadURL(url);
        }
    }

    private void loadURL(String url) {
        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                try {
                    String gemtext = GeminiFetch.fetchWebPage(url);
                    GemtextParser parser = new GemtextParser();
                    GemtextDocument doc = parser.parse(gemtext);

                    return new String[] {doc.getTitle(), HTMLConverter.convertToHTML(doc)};
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void done() {
                String[] data;

                try {
                    data = this.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

                int tabIndex = Tab.this.tabbedPane.indexOfComponent(Tab.this);
                Tab.this.tabbedPane.setTitleAt(tabIndex, data[0]);

                URI uri = URI.create(url);
                String baseUrl = uri.getScheme() + "://" + uri.getHost();
                Tab.this.geminiPanel.setBaseURL(baseUrl);
                Tab.this.geminiPanel.setHTML(data[1]);
                Tab.this.geminiPanel.scrollToTop();
            }
        }.execute();
    }

    public void onClose() {

    }
}
