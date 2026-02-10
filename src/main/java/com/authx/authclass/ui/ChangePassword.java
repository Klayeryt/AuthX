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

public class ChangePassword  extends InteractiveCustomUIPage<ChangePassword.UIEventData> {

    // Path relative to Common/UI/Custom/
    public static final String LAYOUT = "authx/ChangePassword.ui";
    private authxPlugin plugin;
    private final PlayerRef playerRef;
    private LangManager lang;

    public ChangePassword(@Nonnull PlayerRef playerRef, authxPlugin pl) {
        super(playerRef, CustomPageLifetime.CantClose, ChangePassword.UIEventData.CODEC);
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
        cmd.set("#StatusText.Text", lang.get("changepassword.title"));
        cmd.set("#Password.PlaceholderText", lang.get("changepassword.oldpassword"));
        cmd.set("#ConfirmPassword.PlaceholderText", lang.get("changepassword.newpassword"));
        cmd.set("#RefreshButton.Text", lang.get("changepassword.check"));
        cmd.set("#CloseButton.Text", lang.get("changepassword.close"));
        cmd.set("#description.Text", lang.get("changepassword.description"));
        // Bind refresh button
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#RefreshButton", new EventData().append("Action", "refresh").append("@Value", "#Password.Value").append("@ValueConfirm", "#ConfirmPassword.Value"), false);

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
            @Nonnull ChangePassword.UIEventData data
    ) {
        UICommandBuilder cmd = new UICommandBuilder();

        //this.sendUpdate(cmd,false);
        ActionPlayer ap = new ActionPlayer(playerRef,plugin);
        if (data.action == null) return;
        switch (data.action) {
            case "refresh":
                if(data.valueConfirm.length() >= plugin.getSettings().get().CountCharPassword ) {
                    MySQL.Result result = MySQL.ChangePasswordPlayer(playerRef.getUsername(),data.value,data.valueConfirm,ap.getUserId());
                    if (result == MySQL.Result.Success) {
                        NotificationUtil.sendNotification(
                                playerRef.getPacketHandler(),
                                Message.raw(lang.get("changepassword.successful")),
                                NotificationStyle.Success
                        );
                        this.close();
                    }
                    else if (result == MySQL.Result.None) {
                        cmd.set("#Nofication.Text", lang.get("changepassword.type1"));
                        cmd.set("#Nofication.Style.TextColor", "#ff0000");
                        this.sendUpdate(cmd, false);
                    }
                    else if (result == MySQL.Result.No) {
                        cmd.set("#Nofication.Text", lang.get("changepassword.type1"));
                        cmd.set("#Nofication.Style.TextColor", "#ff0000");
                        this.sendUpdate(cmd, false);
                    }
                    else if (result == MySQL.Result.Error) {
                        NotificationUtil.sendNotification(
                                playerRef.getPacketHandler(),
                                Message.raw(lang.get("changepassword.error")),
                                NotificationStyle.Danger
                        );
                    }
                }
                else if (data.valueConfirm.length() <= plugin.getSettings().get().CountCharPassword) {
                    cmd.set("#Nofication.Text",lang.get("changepassword.type2",plugin.getSettings().get().CountCharPassword));
                    cmd.set("#Nofication.Style.TextColor", "#ff0000");
                    this.sendUpdate(cmd, false);
                }
                break;
            case "close":
                this.close();
                break;
        }
    }

    /**
     * Event data class with codec for handling UI events.
     */
    public static class UIEventData {
        public static final BuilderCodec<ChangePassword.UIEventData> CODEC = BuilderCodec.builder(ChangePassword.UIEventData.class, ChangePassword.UIEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (e, v) -> e.action = v, e -> e.action)
                .add()
                .append(new KeyedCodec<>("@Value", Codec.STRING), (r, s) -> r.value = s, r -> r.value)
                .add()
                .append(new KeyedCodec<>("@ValueConfirm", Codec.STRING), (r, s) -> r.valueConfirm = s, r -> r.valueConfirm)
                .add()
                .build();
        private String action;
        private String value;
        private String valueConfirm;
        public UIEventData() {}
    }
}
