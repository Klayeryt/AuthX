package com.authx.authclass.api;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ConfigDB {
    public static final BuilderCodec<ConfigDB> CODEC = BuilderCodec.builder(ConfigDB.class, ConfigDB::new)
            .append(new KeyedCodec<String>("Username", Codec.STRING), (un, value) -> un.dbusername = value, (username) -> username.dbusername)
            .add()
            .append(new KeyedCodec<String>("Port", Codec.STRING), (un, value) -> un.dbport = value, (username) -> username.dbport)
            .add()
            .append(new KeyedCodec<String>("Password", Codec.STRING), (un, value) -> un.dbpassword = value, (username) -> username.dbpassword)
            .add()
            .append(new KeyedCodec<String>("Address", Codec.STRING), (un, value) -> un.dbaddress = value, (username) -> username.dbaddress)
            .add()
            .append(new KeyedCodec<String>("Name", Codec.STRING), (un, value) -> un.dbname = value, (username) -> username.dbname)
            .add()
    .build();
    private String dbusername = "root";
    private String dbport = "3306";
    private String dbpassword = "1231";
    private String dbaddress = "127.0.0.1";
    private String dbname = "server";

    public ConfigDB() {}

    public String getDbusername() {return  this.dbusername;}
    public String getDbport() {return this.dbport;}
    public String getDbpassword() {return this.dbpassword;}
    public String getDbaddress() {return this.dbaddress;}
    public String getDbname() {return this.dbname;}

    public void setDbusername(String dbname) {this.dbusername = dbname;}
    public void setDbport(String port) {this.dbport = port;}
    public void setPassword(String password) {this.dbpassword = password;}
    public void setDbaddress(String address) {this.dbaddress = address;}
    public void setDbname(String name) {this.dbname = name;}
}
