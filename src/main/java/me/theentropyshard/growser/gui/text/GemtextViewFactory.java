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

package me.theentropyshard.growser.gui.text;

import javax.swing.text.*;

public class GemtextViewFactory implements ViewFactory {
    @Override
    public View create(Element element) {
        if (element.getAttributes().getAttribute("preformatted") != null) {
            return new PreformattedView(element);
        }

        if (element.getAttributes().getAttribute("listItem") != null) {
            return new ListItemView(element);
        }

        String kind = element.getName();

        if (kind != null) {
            switch (kind) {
                case AbstractDocument.ContentElementName:
                    return new LabelView(element);
                case AbstractDocument.ParagraphElementName:
                    return new ParagraphView(element);
                case AbstractDocument.SectionElementName:
                    return new BoxView(element, View.Y_AXIS);
                case StyleConstants.ComponentElementName:
                    return new ComponentView(element);
                case StyleConstants.IconElementName:
                    return new IconView(element);
            }
        }

        return new LabelView(element);
    }
}
