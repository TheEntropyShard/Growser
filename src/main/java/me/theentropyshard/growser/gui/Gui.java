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

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import me.theentropyshard.growser.gui.text.GemtextEditorKit;
import me.theentropyshard.growser.utils.swing.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntConsumer;

public class Gui {
    public static final String TITLE = "Growser";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int MIN_TAB_WIDTH = 30;
    public static final int MAX_TAB_WIDTH = 250;

    private final JTabbedPane tabbedPane;
    private final JFrame frame;

    public static JFrame sFrame;

    public Gui() {
        JEditorPane.registerEditorKitForContentType("text/gemini", GemtextEditorKit.class.getName());
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        FlatIntelliJLaf.setup();

        this.registerFont();

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setPreferredSize(new Dimension(Gui.WINDOW_WIDTH, Gui.WINDOW_HEIGHT));
        this.tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, true);
        this.tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, (IntConsumer) value -> {
            ((Tab) this.tabbedPane.getComponentAt(value)).onClose();
            this.tabbedPane.removeTabAt(value);
        });
        this.tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, Gui.MIN_TAB_WIDTH);
        this.tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, Gui.MAX_TAB_WIDTH);

        sFrame = this.frame = new JFrame(Gui.TITLE);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        this.frame.add(this.tabbedPane, BorderLayout.CENTER);
        this.frame.pack();
        SwingUtils.centerWindow(this.frame, 0);
        this.frame.setVisible(true);
    }

    private void registerFont() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, Gui.class.getResourceAsStream("/assets/fonts/roboto/static/Roboto-Regular.ttf"));
            System.out.println(font.getFontName());
            System.out.println(font.getFamily());

            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            env.registerFont(font);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTabbedPane getTabbedPane() {
        return this.tabbedPane;
    }

    public JFrame getFrame() {
        return this.frame;
    }
}
