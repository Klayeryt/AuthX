package com.authx.authclass.ui;

import com.authx.authclass.api.LangManager;
import com.authx.authclass.api.MySQL;
import com.authx.authclass.authxPlugin;
import com.authx.authclass.commands.ActionPlayer;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.NotificationUtil;

import javax.annotation.Nonnull;

public class authxUI extends InteractiveCustomUIPage<authxUI.UIEventData> {

    // Path relative to Common/UI/Custom/
    public static final String LAYOUT = "authx/Login.ui";
    private final PlayerRef playerRef;
    public String Val = "";
    private int count = 0;
    private authxPlugin plugin;
    private LangManager lang;
    // int refreshCount = 0;

    public authxUI(@Nonnull PlayerRef playerRef, authxPlugin pl) {
        super(playerRef, CustomPageLifetime.CantClose, UIEventData.CODEC);
        this.playerRef = playerRef;
        this.plugin = pl;
        this.lang = pl.getLang();
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {

        // Load base layout
        cmd.append(LAYOUT);
        cmd.set("#ServerName.Text", plugin.getSettings().get().NameServer);
        cmd.set("#StatusText.Text", lang.get("auth.title"));
        cmd.set("#Password.PlaceholderText", lang.get("auth.password"));
        cmd.set("#RefreshButton.Text", lang.get("auth.check"));
        cmd.set("#CloseButton.Text", lang.get("auth.close"));
        cmd.set("#description.Text", lang.get("auth.description"));
        // Bind refresh button
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RefreshButton", new EventData().append("Action", "refresh").append("@Value", "#Password.Value"), false);

        // Bind close button
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CloseButton",
                new EventData().append("Action", "close"),
                false
        );
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull UIEventData data
    ) {
        UICommandBuilder cmd = new UICommandBuilder();
        //this.sendUpdate(cmd,false);
        ActionPlayer ap = new ActionPlayer(playerRef,plugin);
        if (data.action == null) return;
        switch (data.action) {

            case "refresh":
                Val = data.value;
                String result = MySQL.GetPlayerResultSet("username",playerRef.getUsername(),"password",Val,"uid",ap.getUserId(),1);
                if (result != null) {
                    NotificationUtil.sendNotification(
                            playerRef.getPacketHandler(),
                            Message.raw(lang.get("auth.successful")),
                            NotificationStyle.Success
                    );
                    this.close();
                }
                else {
                    count++;
                    cmd.set("#TryPassword.Text", lang.get("auth.try",count));
                    this.sendUpdate(cmd, false);
                    if(count == 3) {ap.kick(1);}
                }
                break;
            case "close":
                ap.kick(0);
                break;
        }
    }

    /**
     * Event data class with codec for handling UI events.
     */
    public static class UIEventData {
        public static final BuilderCodec<UIEventData> CODEC = BuilderCodec.builder(UIEventData.class, UIEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action)
                .add()
                .append(new KeyedCodec<>("@Value", Codec.STRING), (r, s) -> r.value = s, r -> r.value)
                .add()
                .build();
        private String action;
        private String value;
        public UIEventData() {}
    }
}