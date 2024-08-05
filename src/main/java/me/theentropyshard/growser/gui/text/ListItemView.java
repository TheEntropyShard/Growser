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

package me.theentropyshard.growser.gui.text;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import java.awt.*;

public class ListItemView extends ParagraphView {
    public ListItemView(Element element) {
        super(element);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        Graphics graphics = g.create();

        Rectangle bounds = a.getBounds();
        graphics.setColor(Color.BLACK);
        int offset = 15;
        int size = g.getFont().getSize() / 2 - 1;
        graphics.fillOval(bounds.x + offset / 2, bounds.y + (bounds.height / 2 - size), size, size);

        graphics.translate(offset, 0);

        super.paint(g, a);
    }
}
