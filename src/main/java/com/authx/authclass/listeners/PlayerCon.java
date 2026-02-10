package com.authx.authclass.listeners;

import com.authx.authclass.api.MySQL;
import com.authx.authclass.authxPlugin;
import com.authx.authclass.ui.RegisterUI;
import com.authx.authclass.ui.authxUI;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.TimeUnit;

/**
 * PlayerCon - Event listener for player events.
 */
public class PlayerCon {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static authxPlugin plugin;

    public PlayerCon(authxPlugin pl) {
        this.plugin = pl;
    }

    public void register(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::PlayerReady);
    }

    private void PlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        Store<EntityStore> store = ref.getStore();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if(MySQL.CheckPlayer(playerRef.getUsername()) == true) {
            authxUI LoginPage = new authxUI(playerRef, plugin);
            event.getPlayer().getPageManager().openCustomPage(ref, store, LoginPage);
        }
        else {
            RegisterUI RegisterPage = new RegisterUI(playerRef, plugin);
            event.getPlayer().getPageManager().openCustomPage(ref, store, RegisterPage);
        }
    }
}
