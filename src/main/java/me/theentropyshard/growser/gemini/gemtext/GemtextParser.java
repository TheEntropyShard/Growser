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

package me.theentropyshard.growser.gemini.gemtext;

import me.theentropyshard.growser.gemini.gemtext.document.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GemtextParser {
    private static final String PREFORMATTED_START = "```";
    private static final String LIST_ITEM_START = "* ";
    private static final String H1_START = "# ";
    private static final String H2_START = "## ";
    private static final String H3_START = "### ";
    private static final String BLOCKQUOTE_START = "> ";
    private static final String LINK_START = "=>";

    public GemtextParser() {

    }

    public GemtextPage parse(String text) {
        List<GemtextElement> elements = new ArrayList<>();

        String[] lines = text.split("\\r?\\n");

        boolean inPreBlock = false;
        String preCaption = null;
        List<String> preContents = new ArrayList<>();
        String title = null;

        GemtextListElement listElement = null;

        for (String line : lines) {
            if (!line.startsWith(GemtextParser.LIST_ITEM_START) && listElement != null) {
                elements.add(listElement);
                listElement = null;
            }

            if (line.startsWith(GemtextParser.PREFORMATTED_START)) {
                if (inPreBlock) {
                    elements.add(new GemtextPreformattedElement(preCaption, String.join("\n", preContents)));
                    preContents = new ArrayList<>();
                } else {
                    String caption = line.substring(3);
                    preCaption = caption.isEmpty() ? null : caption;
                }
                inPreBlock = !inPreBlock;
            } else if (inPreBlock) {
                preContents.add(line);
            } else if (line.startsWith(GemtextParser.LIST_ITEM_START)) {
                if (listElement == null) {
                    listElement = new GemtextListElement();
                }

                listElement.add(line.substring(2));
            } else if (line.startsWith(GemtextParser.H1_START)) {
                String h1Content = line.substring(2);
                if (title == null) {
                    title = h1Content;
                }
                elements.add(new GemtextH1Element(h1Content));
            } else if (line.startsWith(GemtextParser.H2_START)) {
                elements.add(new GemtextH2Element(line.substring(3)));
            } else if (line.startsWith(GemtextParser.H3_START)) {
                elements.add(new GemtextH3Element(line.substring(4)));
            } else if (line.startsWith(GemtextParser.BLOCKQUOTE_START)) {
                elements.add(new GemtextBlockquoteElement(line.substring(2)));
            } else if (line.startsWith(GemtextParser.LINK_START)) {
                line = line.substring(2).trim();
                int spaceIndex = -1;

                for (char c : line.toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        spaceIndex = line.indexOf(c);

                        break;
                    }
                }

                String link;
                String label;

                if (spaceIndex == -1) {
                    link = line;
                    label = null;
                } else {
                    link = line.substring(0, spaceIndex);
                    label = line.substring(spaceIndex).trim();
                }

                elements.add(new GemtextLinkElement(link, label));
            } else {
                elements.add(new GemtextParagraphElement(line));
            }
        }

        if (listElement != null) {
            elements.add(listElement);
        }

        return new GemtextPage(title, elements);
    }
}
