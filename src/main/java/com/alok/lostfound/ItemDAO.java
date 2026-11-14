package com.alok.lostfound;

import java.sql.*;
import java.util.*;

public class ItemDAO {
    public static void add(User u,String title,String desc,String category,String location,String type,String date) throws SQLException {
        String q="INSERT INTO items(title,description,category,location,item_type,item_date,reported_by) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setString(1,title);p.setString(2,desc);p.setString(3,category);p.setString(4,location);
            p.setString(5,type); if(date.isBlank())p.setNull(6,Types.DATE);else p.setDate(6,Date.valueOf(date));
            p.setInt(7,u.id());p.executeUpdate();
        }
    }
    public static List<Item> all(String filter) throws SQLException {
        List<Item> out=new ArrayList<>();
        String q="SELECT i.*,u.full_name FROM items i JOIN users u ON i.reported_by=u.id WHERE (?='ALL' OR i.item_type=?) ORDER BY i.created_at DESC";
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setString(1,filter);p.setString(2,filter);
            try(ResultSet r=p.executeQuery()){while(r.next())out.add(new Item(r.getInt("id"),r.getString("title"),r.getString("description"),
                r.getString("category"),r.getString("location"),r.getString("item_type"),String.valueOf(r.getDate("item_date")),
                r.getString("status"),r.getString("full_name")));}
        } return out;
    }
    public static void claim(User u,int itemId,String message) throws SQLException {
        String q="INSERT INTO claims(item_id,claimant_id,message) VALUES(?,?,?)";
        try(Connection c=Database.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,itemId);p.setInt(2,u.id());p.setString(3,message);p.executeUpdate();
        }
    }
}
