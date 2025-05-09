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

package me.theentropyshard.growser;

import me.theentropyshard.growser.language.Language;
import me.theentropyshard.growser.language.LanguageManager;
import me.theentropyshard.growser.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public class Growser {
    private static final Logger LOG = LogManager.getLogger(Growser.class);

    private final Args args;

    private final Path workDir;
    private final Path languagesDir;

    private final Settings settings;

    private final LanguageManager languageManager;

    public Growser(Args args, String[] rawArgs) {
        this.args = args;

        if (this.args.hasUnknownOptions()) {
            Growser.LOG.warn("Unknown options: {}", this.args);
        }

        Growser.instance = this;

        this.workDir = this.args.getWorkDir();
        this.languagesDir = this.workDir.resolve("languages");

        try {
            this.createDirectories();
        } catch (IOException e) {
            Growser.LOG.fatal("Could not create Growser directories", e);

            System.exit(1);
        }

        this.settings = Settings.load(this.workDir.resolve("settings.json"));

        this.languageManager = new LanguageManager(this.languagesDir);
        this.languageManager.load();

        Language language = this.getLanguage();
        UIManager.put("OptionPane.yesButtonText", language.getString("gui.general.yes"));
        UIManager.put("OptionPane.noButtonText", language.getString("gui.general.no"));
        UIManager.put("OptionPane.okButtonText", language.getString("gui.general.ok"));
        UIManager.put("OptionPane.cancelButtonText", language.getString("gui.general.cancel"));
    }

    private void createDirectories() throws IOException {
        FileUtils.createDirectoryIfNotExists(this.workDir);
        FileUtils.createDirectoryIfNotExists(this.languagesDir);
    }

    private static Growser instance;

    public static Growser getInstance() {
        return Growser.instance;
    }

    public Args getArgs() {
        return this.args;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Language getLanguage() {
        return this.languageManager.getLanguage(this.settings.language);
    }
}
