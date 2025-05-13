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

package me.theentropyshard.growser.gemini.gemtext.document;

public class GemtextLinkElement extends GemtextElement {
    private final String link;
    private final String label;

    public GemtextLinkElement(String link, String label) {
        super(GemtextElementType.LINK);

        this.link = link;
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label == null || this.label.trim().isEmpty() ? this.link : (this.label + " (" + this.link + ")");
    }

    public String getLink() {
        return this.link;
    }

    public String getLabel() {
        return this.label;
    }
}
