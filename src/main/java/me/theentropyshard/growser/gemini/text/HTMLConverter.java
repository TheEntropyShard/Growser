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

package me.theentropyshard.growser.gemini.text;

import me.theentropyshard.growser.gemini.text.document.*;

import java.util.List;

public class HTMLConverter {
    public static String convertToHTML(GemtextDocument doc) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");

        List<GemtextElement> elements = doc.getElements();

        boolean makingList = false;

        for (GemtextElement element : elements) {
            if (element.getType() == GemtextElement.Type.LIST_ITEM && !makingList) {
                makingList = true;
                html.append("<ul>");
            }

            if (element.getType() != GemtextElement.Type.LIST_ITEM && makingList) {
                makingList = false;
                html.append("</ul>");
            }

            switch (element.getType()) {
                case TEXT:
                    html.append("<p>");
                    html.append(HTMLConverter.escapeHTML(((GemtextParagraphElement) element).getText()));
                    html.append("</p>");

                    break;
                case LINK:
                    GemtextLinkElement link = (GemtextLinkElement) element;

                    html.append("<a href=\"").append(HTMLConverter.escapeHTML(link.getLink())).append("\">");

                    if (link.getLabel() != null) {
                        html.append(HTMLConverter.escapeHTML(link.getLabel()));
                    } else {
                        html.append(HTMLConverter.escapeHTML(link.getLink()));
                    }

                    html.append("</a>");

                    break;
                case LIST_ITEM:
                    GemtextListItemElement listItem = (GemtextListItemElement) element;

                    html.append("<li>");
                    html.append(HTMLConverter.escapeHTML(listItem.getText()));
                    html.append("</li>");

                    break;
                case H1:
                    GemtextH1Element h1 = (GemtextH1Element) element;

                    html.append("<center><h1>");
                    html.append(HTMLConverter.escapeHTML(h1.getText()));
                    html.append("</h1></center>");

                    break;
                case H2:
                    GemtextH2Element h2 = (GemtextH2Element) element;

                    html.append("<h2>");
                    html.append(HTMLConverter.escapeHTML(h2.getText()));
                    html.append("</h2>");

                    break;
                case H3:
                    GemtextH3Element h3 = (GemtextH3Element) element;

                    html.append("<h3>");
                    html.append(HTMLConverter.escapeHTML(h3.getText()));
                    html.append("</h3>");

                    break;
                case BLOCKQUOTE:
                    GemtextBlockquoteElement blockquote = (GemtextBlockquoteElement) element;

                    html.append("<blockquote>");
                    html.append(HTMLConverter.escapeHTML(blockquote.getText()));
                    html.append("</blockquote>");

                    break;
                case PREFORMATTED:
                    GemtextPreformattedElement pre = (GemtextPreformattedElement) element;

                    html.append("<pre aria-label=\"").append(pre.getCaption()).append("\">");
                    html.append(HTMLConverter.escapeHTML(pre.getText()));
                    html.append("</pre>");

                    break;
                default:
                    throw new IllegalArgumentException("Unreachable");
            }
        }

        if (makingList) {
            html.append("</ul>");
        }

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private static String escapeHTML(String s) {
        return s
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("'", "&#039;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
