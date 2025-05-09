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

import javax.swing.text.*;

public class GemtextViewFactory implements ViewFactory {
    @Override
    public View create(Element element) {
        String kind = element.getName();

        if (kind != null) {
            return switch (kind) {
                case AbstractDocument.ContentElementName -> new LabelView(element);
                case AbstractDocument.ParagraphElementName -> new ParagraphView(element);
                case AbstractDocument.SectionElementName -> new BoxView(element, View.Y_AXIS);
                case StyleConstants.ComponentElementName -> new ComponentView(element);
                case StyleConstants.IconElementName -> new IconView(element);

                default -> throw new IllegalArgumentException("Unsupported kind of element: " + kind);
            };
        }

        return new LabelView(element);
    }
}
