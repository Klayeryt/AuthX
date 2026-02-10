package com.authx.authclass.api;

//Hytale API
import com.authx.authclass.authxPlugin;

import com.authx.authclass.settings.mainsettings;
import com.hypixel.hytale.logger.HytaleLogger;
import com.mysql.cj.jdbc.Driver;
//
// SQL lib
import java.sql.*;
import java.sql.DriverManager;
//
// Other
import java.util.logging.Level;
//
public class MySQL {
    public static Connection con; // Connnection to MYSQL database
    static HytaleLogger logger = HytaleLogger.forEnclosingClass(); // Console Sender(Message for player)
    private final ConfigDB plugin;
    private static mainsettings configset;

    // MAIN METHOD
    public enum Result {
        None,
        Success,
        Error,
        No
    }

    public MySQL(authxPlugin plugin) {
       this.plugin = plugin.getConfig().get();
       configset = plugin.getSettings().get();
    }


    public void ConnectDB() {
        if(!isConnected()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                DriverManager.registerDriver(new Driver()); // Явная регистрация
                con = DriverManager.getConnection("jdbc:mysql://"
                                + plugin.getDbaddress()
                                + ":" + plugin.getDbport()
                                + "/" + plugin.getDbname()
                                + "?useSSL=false"                // Для тестов (в продакшене настроить SSL)
                                + "&serverTimezone=UTC"         // Избегает проблем с часовыми поясами
                                + "&allowPublicKeyRetrieval=false" // Для новых версий MySQL
                                + "&autoReconnect=true",
                        plugin.getDbusername(),
                        plugin.getDbpassword());
                logger.at(Level.INFO).log("Connection Successful");
            }
            catch (SQLException e) {
                logger.at(Level.WARNING).log(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void DisconnectDB() {
        if(isConnected()) {
            try {
                con.close();
                logger.at(Level.WARNING).log("Connection failed");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return (con == null ? false : true);
    }

    public static Connection getConnection() {
        return con;
    }

    public static boolean Query(String row) {
        if(isConnected()) {
            try {
                Statement st = getConnection().createStatement();
                int rs = st.executeUpdate(row);
                return true;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static String GetPlayer(String username) {
        if(isConnected()) {
            try {
                String query = "SELECT * FROM " + configset.NameDbAccounts + " WHERE username = '" + username + "';";
                Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next() == false) {return null;} else {
                    return rs.getString(1);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static boolean GetPlayerFindPassword(String uid, String password) {
        if(isConnected()) {
            try {
                String query = "SELECT * FROM " + configset.NameDbAccounts + " WHERE uid = '" + uid + "' AND password = '" + password + "';";
                Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next() == false) {return false;} else {
                    return true;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static String GetPlayerResultSet(String UnColumn,String ValueUn, String PassColumn, String ValuePass, String UidColumn, String ValueUID, int index) {
        if(isConnected()) {
            try {
                String query = "SELECT * FROM " + configset.NameDbAccounts + " WHERE " + UnColumn + " = '" + ValueUn + "' AND " + PassColumn + " = '" + ValuePass + "' AND " + UidColumn + " = '" + ValueUID + "';";
                Statement st = getConnection().createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs.next() == false) {return null;} else {
                    return rs.getString(index);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static boolean CheckPlayer(String Username) {
        if(GetPlayer(Username) != null) {return  true;} else {return  false;}
    }

    public static boolean RegisterPlayer(String ValueUn, String ValuePass, String ValueUID) {
        if(isConnected() && !CheckPlayer(ValueUn)) {
            try {
                String query = "INSERT INTO " + configset.NameDbAccounts + "(id,username, password, uid) VALUES (default,'" + ValueUn + "', '" + ValuePass + "', '" + ValueUID + "')";
                Statement st = getConnection().createStatement();
                int rs = st.executeUpdate(query);
                if(rs == 0) {return false;} else {
                    return true;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }

    public static Result ChangePasswordPlayer(String ValueUn, String ValuePass, String newPassword, String ValueUID) {
        if(isConnected() && CheckPlayer(ValueUn) && GetPlayerFindPassword(ValueUID, ValuePass)) {
            try {
                String query = "UPDATE accounts set password = '" + newPassword +"' WHERE uid = '" + ValueUID + "';";
                Statement st = getConnection().createStatement();
                int rs = st.executeUpdate(query);
                if(rs == 0) {return Result.None;} else {
                    return Result.Success;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                return Result.Error;
            }
        }
        else {
            return Result.No;
        }
    }


}
