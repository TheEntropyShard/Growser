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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GemtextListElement extends GemtextElement implements Iterable<String> {
    private final List<String> items;

    public GemtextListElement() {
        super(GemtextElementType.LIST);

        this.items = new ArrayList<>();
    }

    public void add(String item) {
        this.items.add(item);
    }

    public String get(int index) {
        return this.items.get(index);
    }

    public int size() {
        return this.items.size();
    }

    @Override
    public Iterator<String> iterator() {
        return this.items.iterator();
    }

    @Override
    public String toString() {
        return this.getItems().stream().map(s -> "* " + s).collect(Collectors.joining("\n"));
    }

    public List<String> getItems() {
        return this.items;
    }
}
