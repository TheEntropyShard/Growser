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

import com.formdev.flatlaf.ui.FlatScrollPaneUI;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.net.URL;

public class GeminiPanel extends JPanel implements HyperlinkListener {
    private final JTextPane textPane;
    private final JScrollPane scrollPane;
    private final JTabbedPane tabbedPane;
    private final AddressBar addressBar;

    private String baseURL;

    public GeminiPanel(JTabbedPane tabbedPane, AddressBar addressBar) {
        super(new BorderLayout());

        this.tabbedPane = tabbedPane;
        this.addressBar = addressBar;

        this.textPane = new JTextPane() {
            @Override
            protected void paintComponent(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                super.paintComponent(g);
            }
        };

        DefaultCaret caret = (DefaultCaret) this.textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        this.textPane.setFont(this.textPane.getFont().deriveFont(14.0f));
        this.textPane.setEditorKit(new HTMLEditorKit());
        this.textPane.setContentType("text/html");
        this.textPane.setEditable(false);
        this.textPane.addHyperlinkListener(this);

        this.scrollPane = new JScrollPane(
                this.textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        this.scrollPane.setBorder(null);
        this.scrollPane.setUI(new FlatScrollPaneUI() {
            @Override
            protected MouseWheelListener createMouseWheelListener() {
                if (this.isSmoothScrollingEnabled()) {
                    return new SmoothScrollMouseWheelListener(this.scrollpane.getVerticalScrollBar());
                } else {
                    return super.createMouseWheelListener();
                }
            }
        });

        this.add(this.scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        HyperlinkEvent.EventType eventType = e.getEventType();

        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            URL url = e.getURL();

            String sUrl;

            if (url == null) {
                String desc = e.getDescription();

                String baseUrl = this.addressBar.getUrl();

                if ((baseUrl.endsWith("/") && !desc.startsWith("/")) || (!baseUrl.endsWith("/") && desc.startsWith("/"))) {
                    sUrl = baseUrl + desc;
                } else if (baseUrl.endsWith("/") && desc.startsWith("/")) {
                    sUrl = baseUrl + desc.substring(1);
                } else if (!baseUrl.endsWith("/") && !desc.startsWith("/")) {
                    sUrl = baseUrl + "/" + desc;
                } else {
                    System.out.println("WARN: Unknown error. baseUrl: " + baseUrl + ", desc: " + desc);

                    return;
                }
            } else {
                sUrl = url.toString();
            }

            if (sUrl.startsWith("http://") || sUrl.startsWith("https://")) {
                System.out.println("WARN: http[s] scheme is not supported");

                return;
            }

            this.tabbedPane.addTab("Title", new Tab(sUrl, this.tabbedPane));
        }
    }

    public void scrollToTop() {
        JScrollBar scrollBar = this.scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMinimum());
    }

    public void scrollToBottom() {
        JScrollBar scrollBar = this.scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    public void setHTML(String text) {
        this.textPane.setText(text);
    }

    public JTextPane getTextPane() {
        return this.textPane;
    }
}
