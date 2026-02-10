package com.authx.authclass.commands;

import com.authx.authclass.api.MySQL;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import com.authx.authclass.authxPlugin;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.jline.jansi.io.ColorsAnsiProcessor;
import org.jline.utils.Colors;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * /auth reload - Reload plugin configuration
 */
public class ReloadSubCommand extends CommandBase {

    public ReloadSubCommand() {
        super("reload", "Reload plugin configuration");
        this.setPermissionGroup(null);
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        authxPlugin plugin = authxPlugin.getInstance();
        if (plugin == null) {
            context.sendMessage(Message.raw("Error: Plugin not loaded"));
            return;
        }
        if(!context.sender().hasPermission("authx.*")) {
            context.sendMessage(Message.raw("У вас нет прав использовать данную команду!").color(new Color(255,0,0)));
            return;
        }
        context.sendMessage(Message.raw("Cleaning database authx..."));
        MySQL.Query("DROP TABLE accounts");
        MySQL.Query("CREATE TABLE IF NOT EXISTS accounts(id serial primary key,username VARCHAR(100),password VARCHAR(100), uid VARCHAR(100));");
        context.sendMessage(Message.raw("AuthX database successful cleaning"));
    }
}