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

import me.theentropyshard.growser.gemini.GeminiFetch;
import me.theentropyshard.growser.gemini.text.GemtextParser;
import me.theentropyshard.growser.gemini.text.document.GemtextPage;
import me.theentropyshard.growser.gui.debug.StructureDialog;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoadURLWorker extends SwingWorker<String[], Void> {
    private final String url;
    private final Tab tab;
    private final Listener listener;

    public LoadURLWorker(String url, Tab tab, Listener listener) {
        this.url = url;
        this.tab = tab;
        this.listener = listener;
    }

    public interface Listener {
        void onException(Exception e);
    }

    @Override
    protected String[] doInBackground() {
        try {
            String gemtext = GeminiFetch.fetchWebPage(this.url);
            GemtextParser parser = new GemtextParser();
            GemtextPage doc = parser.parse(gemtext);

            return new String[]{doc.getTitle(), gemtext};
            //return new String[]{doc.getTitle(), HTMLConverter.convertToHTML(doc)};
        } catch (IOException e) {
            this.listener.onException(e);

            return null;
        }
    }

    @Override
    protected void done() {
        String[] data = null;

        try {
            data = this.get();
        } catch (InterruptedException | ExecutionException e) {
            this.listener.onException(e);
        }

        if (data == null) {
            return;
        }

        int tabIndex = this.tab.getTabbedPane().indexOfComponent(this.tab);
        this.tab.getTabbedPane().setTitleAt(tabIndex, data[0]);
        this.tab.getGeminiPanel().setHTML(data[1]);
        this.tab.getGeminiPanel().scrollToTop();
        this.tab.getGeminiPanel().requestFocus();

        JTextPane textPane = this.tab.getGeminiPanel().getTextPane();
        //new StructureDialog(Gui.sFrame, textPane).setVisible(true);
    }
}
