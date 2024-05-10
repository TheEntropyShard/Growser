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

package me.theentropyshard.growser.gemini.text.document;

public class GemtextPreformattedElement extends GemtextElement {
    private final String caption;
    private final String text;

    public GemtextPreformattedElement(String caption, String text) {
        super(Type.PREFORMATTED);

        this.caption = caption;
        this.text = text;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getText() {
        return this.text;
    }
}
