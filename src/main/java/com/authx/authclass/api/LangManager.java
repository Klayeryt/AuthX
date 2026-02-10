package com.authx.authclass.api;

import com.hypixel.hytale.logger.HytaleLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;

public class LangManager {
    private final Properties messages = new Properties();
    private final HytaleLogger logger = HytaleLogger.getLogger();
    private String currentLang;

    public LangManager(String lang) {
        this.currentLang = lang;
    }

    public void load() {
        try (InputStream is = getClass().getResourceAsStream("/" + this.currentLang + "_lang.properties")) {
                if(is == null) {
                    return;
                }
                messages.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            logger.at(Level.WARNING).log(e.getMessage());
        }
    }

    public String get(String key, Object... args) {
        String template = messages.getProperty(key);
        if(template == null) {
            return "[" + key + "]";
        }
        return java.text.MessageFormat.format(template,args);
    }
}
