package com.patika.Model;

import com.patika.Helper.DBConnector;
import com.patika.Helper.Helper;

import java.sql.*;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String uname;
    private String pass;
    private String type;

    public User(){}

    public User(int id, String name, String uname, String pass, String type) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<User> getList(){
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user1";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            User obj;
            while(rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));

                userList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }
    public static ArrayList<User> getListOnlyEducator(){
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user1 WHERE type='educator'";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            User obj;
            while(rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));

                userList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }
    public static boolean add(String name, String uname, String pass, String type){
        String query = "INSERT INTO user1 (name,uname,pass,type) VALUES (?,?,?,?)";
        User findUser = User.getFetch(uname);
        if(findUser != null){
            Helper.showMessage("Bu kullanıcı adı mevcut. Lütfen farklı bir kullanıcı adı giriniz.");
            return false;
        }

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,uname);
            pr.setString(3,pass);
            pr.setObject(4,type, Types.OTHER);

            int response = pr.executeUpdate();

            if(response == -1){
                Helper.showMessage("error");
            }
            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
    public static boolean add(int patika_id, int user_id){
        String query = "INSERT INTO student (patika_id,user_id) VALUES (?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,patika_id);
            pr.setInt(2,user_id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static User getFetch(String uname){
        User obj = null;
        String query = "SELECT * FROM user1 WHERE uname = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static User getFetch(String uname,String pass){
        User obj = null;
        String query = "SELECT * FROM user1 WHERE uname = ? AND pass=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            pr.setString(2, pass);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                switch (rs.getString("type")){
                    case "operator":
                        obj = new Operator();
                        break;
                    case "educator":
                        obj = new Educator();
                        break;
                    case "student":
                        obj = new Student();
                        break;
                    default:
                        obj = new User();
                }

                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
    public static User getFetch(int id){
        User obj = null;
        String query = "SELECT * FROM user1 WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM user1 WHERE id = ?";
        ArrayList<Course> courseList = Course.getListByUser(id);
        for(Course c : courseList){
            Course.delete(c.getId());
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate()!=-1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean update(int id, String name , String uname,String pass, String type){
        String query = "UPDATE user1 SET name = ?, uname=?, pass=?, type = ? WHERE id = ?";
        User findUser = User.getFetch(uname);
        if(findUser != null && findUser.getId() != id){
            Helper.showMessage("Bu kullanıcı adı mevcut. Lütfen farklı bir kullanıcı adı giriniz.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,uname);
            pr.setString(3,pass);
            pr.setObject(4,type,Types.OTHER);
            pr.setInt(5,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static ArrayList<User> searchUserList(String query){
        ArrayList<User> userList = new ArrayList<>();
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            User obj;
            while(rs.next()){
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setType(rs.getString("type"));

                userList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }
    public static String searchQuery(String name, String uname, String type){
        String query = "SELECT * FROM user1 WHERE name ILIKE '%{{name}}%' AND uname ILIKE '%{{uname}}%'";
        query = query.replace("{{name}}",name);
        query = query.replace("{{uname}}",uname);
        if(type != "Tümü"){
            query+=" AND type = '{{type}}'";
            query = query.replace("{{type}}",type);
        }
        return query;
    }
 }
