package com.authx.authclass.commands;

import com.authx.authclass.authxPlugin;
import com.authx.authclass.listeners.PlayerCon;
import com.authx.authclass.ui.ChangePassword;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * ChangePasswordSubCommand - Command collection for /.
 * <p>
 * Usage:
 * - / help - Show available commands
 */
public class ChangePasswordCommand extends CommandBase {

    public ChangePasswordCommand() {
        super("changepassword", "Change your password");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        if (!context.isPlayer()) {
            context.sendMessage(Message.raw("This command requires a player."));
            return;
        }
        Player player = context.senderAs(Player.class);
        Ref<EntityStore> ref = context.senderAsPlayerRef();
        Store<EntityStore> store = ref.getStore();

        if(player == null) {
            context.sendMessage(Message.raw("Error: Plugin not loaded"));
            return;
        }

        authxPlugin plugin = authxPlugin.getInstance();
        if (plugin == null) {
            context.sendMessage(Message.raw("Error: Plugin not loaded"));
            return;
        }
        if(!context.sender().hasPermission("authx.changepassword")) {
            context.sendMessage(Message.raw("У вас нет прав использовать данную команду!").color(new Color(255,0,0)));
            return;
        }
        if(ref == null) {
            context.sendMessage(Message.raw(ref.toString()));
        }
        player.getWorld().execute(() -> {
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            ChangePassword cp = new ChangePassword(playerRef != null ? playerRef : null,plugin);
            player.getPageManager().openCustomPage(ref,store,cp);
    });
    }
}
