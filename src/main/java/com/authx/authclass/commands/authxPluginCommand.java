package com.authx.authclass.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class authxPluginCommand extends AbstractCommandCollection {

    public authxPluginCommand() {
        super("auth", "authx plugin commands");
        this.addSubCommand(new ReloadSubCommand());
        this.addSubCommand(new HelpCommand());
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}