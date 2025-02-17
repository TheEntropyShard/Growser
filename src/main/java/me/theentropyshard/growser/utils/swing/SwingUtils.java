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

package me.theentropyshard.growser.utils.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SwingUtils {
    private static final Map<String, Icon> ICON_CACHE = new HashMap<>();

    public static Icon getIcon(String path) {
        if (SwingUtils.ICON_CACHE.containsKey(path)) {
            return SwingUtils.ICON_CACHE.get(path);
        }

        Icon icon = new ImageIcon(Objects.requireNonNull(SwingUtils.class.getResource(path)));
        SwingUtils.ICON_CACHE.put(path, icon);

        return icon;
    }

    public static void addKeystroke(KeyStroke keyStroke, JComponent component, int condition, String key, Action action) {
        InputMap inputMap = component.getInputMap(condition);
        inputMap.put(keyStroke, key);

        ActionMap actionMap = component.getActionMap();
        actionMap.put(key, action);
    }

    public static Icon loadIconFromBase64(String base64) {
        if (base64 == null) {
            return null;
        }

        byte[] decoded = Base64.getMimeDecoder().decode(base64);
        return new ImageIcon(decoded);
    }

    public static BufferedImage getImage(String path) {
        try (InputStream inputStream = Objects.requireNonNull(SwingUtils.class.getResourceAsStream(path))) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void centerWindow(Window window, int screen) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] allDevices = env.getScreenDevices();

        if (screen < 0 || screen >= allDevices.length) {
            screen = 0;
        }

        Rectangle bounds = allDevices[screen].getDefaultConfiguration().getBounds();
        window.setLocation(
                ((bounds.width - window.getWidth()) / 2) + bounds.x,
                ((bounds.height - window.getHeight()) / 2) + bounds.y
        );
    }

    public static void removeActionListeners(AbstractButton button) {
        for (ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }

    public static void setJTableColumnsWidth(JTable table, double... percentages) {
        TableColumnModel columnModel = table.getColumnModel();
        int tablePreferredWidth = columnModel.getTotalColumnWidth();

        double total = 0;
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }
    }

    public static Action newAction(ActionListener listener) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        };
    }
}
