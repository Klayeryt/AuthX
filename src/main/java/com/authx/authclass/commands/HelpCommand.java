package com.authx.authclass.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;
import java.awt.*;

public class HelpCommand extends CommandBase {

    public HelpCommand() {
        super("help", "Help with plugin AuthX");
        setPermissionGroup(null);
    }
    @Override
    protected boolean canGeneratePermission() {
        return false;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        StringBuilder raw = new StringBuilder();
        raw.append("[AuthX] Help Command:\n");
        raw.append("/auth reload(required permission authx.*) - reload database plugin's\n");
        raw.append("/changepassword - Change your password in database");
        context.sendMessage(Message.raw(raw.toString()).color(new Color(42,212,30)));
    }
}
