package com.alok.lostfound;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Database {
    private static String host="localhost", port="3306", name="lost_found_db", user="root", password="";
    static {
        try (InputStream in = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties p = new Properties();
            if (in != null) p.load(in);
            host=p.getProperty("db.host",host); port=p.getProperty("db.port",port);
            name=p.getProperty("db.name",name); user=p.getProperty("db.user",user);
            password=p.getProperty("db.password","");
        } catch (Exception e) { throw new RuntimeException("Could not read db.properties",e); }
    }
    public static Connection getConnection() throws SQLException {
        String url="jdbc:mysql://"+host+":"+port+"/"+name+"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        return DriverManager.getConnection(url,user,password);
    }
    public static void testConnection() throws SQLException {
        try(Connection c=getConnection()) { if(!c.isValid(3)) throw new SQLException("Connection validation failed"); }
    }
}
