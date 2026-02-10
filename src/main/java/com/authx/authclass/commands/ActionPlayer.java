package com.authx.authclass.commands;

import com.authx.authclass.api.LangManager;
import com.authx.authclass.authxPlugin;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class ActionPlayer {
    private PlayerRef playerRef;
    private LangManager lang;
    public ActionPlayer(@Nonnull PlayerRef playerRef, @Nonnull authxPlugin pl) {
        this.playerRef = playerRef;
        lang = pl.getLang();
    }

    public void kick(int Reason) {if(Reason == 0) {playerRef.getPacketHandler().disconnect(lang.get("kick.mes0"));} else if (Reason == 1) {playerRef.getPacketHandler().disconnect(lang.get("kick.mes1"));}}

    public String getUserId() {
        return playerRef.getUuid().toString();
    }
}
