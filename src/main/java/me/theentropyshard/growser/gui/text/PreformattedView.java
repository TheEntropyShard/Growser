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

import javax.swing.text.*;
import java.awt.*;

public class PreformattedView extends ParagraphView {
    public PreformattedView(Element element) {
        super(element);
    }

    @Override
    public void paint(Graphics g, Shape allocation) {
        Rectangle bounds = allocation.getBounds();

        Color color = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(bounds.x, bounds.y, this.getWidth(), bounds.height);

        g.setColor(color);
        super.paint(g, allocation);
    }

    public boolean isVisible() {
        return true;
    }

    public float getMinimumSpan(int axis) {
        return this.getPreferredSpan(axis);
    }

    public int getResizeWeight(int axis) {
        switch (axis) {
            case View.X_AXIS:
                return 1;
            case View.Y_AXIS:
                return 0;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }

    public float getAlignment(int axis) {
        if (axis == View.X_AXIS) {
            return 0;
        }

        return super.getAlignment(axis);
    }

    public float nextTabStop(float x, int tabOffset) {
        if (this.getTabSet() == null && StyleConstants.getAlignment(this.getAttributes()) == StyleConstants.ALIGN_LEFT) {
            return this.getPreTab(x, tabOffset);
        }

        return super.nextTabStop(x, tabOffset);
    }

    protected float getPreTab(float x, int tabOffset) {
        Document d = this.getDocument();
        View v = this.getViewAtPosition(tabOffset, null);
        if ((d instanceof StyledDocument) && v != null) {
            Font f = ((StyledDocument) d).getFont(v.getAttributes());
            Container c = this.getContainer();
            FontMetrics fm = c.getFontMetrics(f);
            int width = this.getCharactersPerTab() * fm.charWidth('W');
            int tb = (int) this.getTabBase();
            return (float) ((((int) x - tb) / width + 1) * width + tb);
        }

        return 10.0f + x;
    }

    protected int getCharactersPerTab() {
        return 8;
    }
}