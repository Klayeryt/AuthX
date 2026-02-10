package com.authx.authclass;

import com.authx.authclass.api.ConfigDB;
import com.authx.authclass.api.LangManager;
import com.authx.authclass.api.MySQL;
import com.authx.authclass.commands.ChangePasswordCommand;
import com.authx.authclass.listeners.PlayerCon;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.authx.authclass.settings.mainsettings;

import com.authx.authclass.commands.authxPluginCommand;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * authx - A Hytale server plugin.
 */
public class authxPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static authxPlugin instance;
    private final Config<ConfigDB> config = this.withConfig("AuthX", ConfigDB.CODEC);
    private final Config<mainsettings> configsettings = this.withConfig("Setting", mainsettings.CODEC);
    private MySQL mySQL;
    private LangManager lang;

    public authxPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public Config<ConfigDB> getConfig() {
        return config;
    }
    public Config<mainsettings> getSettings() {return configsettings;}
    /**
     * Get the plugin instance.
     * @return The plugin instance
     */
    public static authxPlugin getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        LOGGER.at(Level.INFO).log("[AuthX] Setting up...");
        config.save();
        configsettings.save();
        // Register commands
        registerCommands();

        mySQL = new MySQL(this);
        mySQL.ConnectDB();
        lang = new LangManager(configsettings.get().getLanguage());
        this.lang.load();

        // Register event listeners
        registerListeners();

        LOGGER.at(Level.INFO).log("[authx] Setup complete!");
    }

    public LangManager getLang() {
        return lang;
    }

    /**
     * Register plugin commands.
     */
    private void registerCommands() {
        try {
            getCommandRegistry().registerCommand(new authxPluginCommand());
            getCommandRegistry().registerCommand(new ChangePasswordCommand());
            LOGGER.at(Level.INFO).log("[authx] Registered /auth command");
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).withCause(e).log("[authx] Failed to register commands");
        }
    }

    /**
     * Register event listeners.
     */
    private void registerListeners() {
        EventRegistry eventBus = getEventRegistry();

        try {
            new PlayerCon(this).register(eventBus);
            LOGGER.at(Level.INFO).log("[authx] Registered player event listeners");
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).withCause(e).log("[authx] Failed to register listeners");
        }
    }

    @Override
    protected void start() {
        LOGGER.at(Level.INFO).log("[AuthX] Started!");
        mySQL.Query("CREATE TABLE IF NOT EXISTS " + configsettings.get().NameDbAccounts + "(id serial primary key,username VARCHAR(100),password VARCHAR(100), uid VARCHAR(100));");
    }

    @Override
    protected void shutdown() {
        LOGGER.at(Level.INFO).log("[AuthX] Shutting down...");
        instance = null;
        mySQL.DisconnectDB();
    }
}