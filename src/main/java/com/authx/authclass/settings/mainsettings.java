package com.authx.authclass.settings;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class mainsettings {
    public static final BuilderCodec<mainsettings> CODEC = BuilderCodec.builder(mainsettings.class,mainsettings::new)
            .append(new KeyedCodec<String>("NameAccounts", Codec.STRING), (v,s) -> v.NameDbAccounts = s, (v) -> v.NameDbAccounts)
            .add()
            .append(new KeyedCodec<String>("NameServer", Codec.STRING), (v,s) -> v.NameServer = s, (v) -> v.NameServer)
            .add()
            .append(new KeyedCodec<Integer>("MinPasswordSymbols", Codec.INTEGER), (v,s) -> v.CountCharPassword = s, (v) -> v.CountCharPassword)
            .add()
            .append(new KeyedCodec<String>("Language", Codec.STRING), (v,s) -> v.Language = s, (v) -> v.Language)
            .add()
            .build();

    // Variables
    public String NameDbAccounts = "accounts";
    public String NameServer = "AuthX";
    public int CountCharPassword = 6;
    public String Language = "en";
    //Getter
    public String getNameDbAccounts() {return NameDbAccounts;}
    public String getNameServer() {return NameServer;}
    public int getCountCharPassword() {return CountCharPassword;}
    public String getLanguage() {return Language;}
    //Setter
    public void setNameDbAccounts(String name) {NameDbAccounts = name;}
    public void setNameServer(String name) {NameServer = name;}
    public void setCountCharPassword(int count) {CountCharPassword = count;}
    public void setLanguage(String lang) {Language = lang;}
}
