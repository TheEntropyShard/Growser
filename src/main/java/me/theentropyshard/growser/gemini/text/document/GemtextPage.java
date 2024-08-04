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

import java.util.List;

public class GemtextPage {
    private final String title;
    private final List<GemtextElement> elements;

    public GemtextPage(String title, List<GemtextElement> elements) {
        this.title = title;
        this.elements = elements;
    }

    @Override
    public String toString() {
        return this.elements.toString();
    }

    public String getTitle() {
        return this.title;
    }

    public List<GemtextElement> getElements() {
        return this.elements;
    }
}
