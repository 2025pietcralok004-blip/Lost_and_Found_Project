package com.alok.lostfound;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class Auth {
    public static String hash(String value) {
        try {
            byte[] b=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder s=new StringBuilder();
            for(byte x:b) s.append(String.format("%02x",x));
            return s.toString();
        } catch(Exception e) { throw new RuntimeException(e); }
    }
    public static int register(String name,String email,String password) throws SQLException {
        String sql="INSERT INTO users(full_name,email,password_hash) VALUES(?,?,?)";
        try(Connection c=Database.getConnection(); PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1,name); p.setString(2,email); p.setString(3,hash(password)); p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getInt(1);}
        }
    }
    public static User login(String email,String password) throws SQLException {
        String sql="SELECT id,full_name,email,role FROM users WHERE email=? AND password_hash=?";
        try(Connection c=Database.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,email); p.setString(2,hash(password));
            try(ResultSet r=p.executeQuery()) {
                if(r.next()) return new User(r.getInt("id"),r.getString("full_name"),r.getString("email"),r.getString("role"));
            }
        }
        return null;
    }
}
