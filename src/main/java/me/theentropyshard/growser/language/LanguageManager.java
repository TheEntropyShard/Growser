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

package me.theentropyshard.growser.language;

import me.theentropyshard.growser.utils.FileUtils;
import me.theentropyshard.growser.utils.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class LanguageManager {
    private static final Logger LOG = LogManager.getLogger(LanguageManager.class);

    private final Path languagesDir;
    private final Map<String, Language> languages;

    public LanguageManager(Path languagesDir) {
        this.languagesDir = languagesDir;

        this.languages = new LinkedHashMap<>();
    }

    public Language getLanguage(String name) {
        if (name == null) {
            LanguageManager.LOG.warn("Null value passed to LanguageManager.getLanguage(String)");

            return this.languages.get("English");
        }

        Language language = this.languages.get(name);

        if (language == null) {
            LanguageManager.LOG.warn("Could not find language '" + name + "'");

            return this.languages.get("English");
        }

        return language;
    }

    public void load() {
        this.loadBuiltinLanguages();

        this.loadExternalLanguages();
    }

    private void loadBuiltinLanguages() {
        for (String lang : new String[]{"en_US", "ru_RU"}) {
            String resourcePath = "/lang/" + lang + ".json";

            String json = null;

            try {
                json = ResourceUtils.readToString(resourcePath);
            } catch (IOException e) {
                LanguageManager.LOG.warn("Cannot load " + resourcePath + ": " + e.getMessage());
            }

            if (json == null) {
                continue;
            }

            Language language = new Language(json);
            this.languages.put(language.getName(), language);
        }
    }

    private void loadExternalLanguages() {
        try {
            for (Path languageJsonFile : FileUtils.list(this.languagesDir)) {
                try {
                    String json = FileUtils.readUtf8(languageJsonFile);

                    Language language = new Language(json);
                    String name = language.getName();

                    if (this.languages.containsKey(name)) {
                        LanguageManager.LOG.warn("Duplicate language is in 'languages' folder, it won't be loaded! Duplicated name: " + name);

                        continue;
                    }

                    this.languages.put(name, language);
                } catch (IOException e) {
                    LanguageManager.LOG.error("Could not load custom languages", e);
                }
            }
        } catch (IOException e) {
            LanguageManager.LOG.error("Could not list '" + this.languagesDir + "'", e);
        }
    }

    public Map<String, Language> getLanguages() {
        return this.languages;
    }
}
