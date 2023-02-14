package com.patika.Model;

import com.patika.Helper.DBConnector;

import javax.swing.text.Style;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SimpleTimeZone;

public class Patika {
    private int id;
    private String name;


    public Patika(int id, String name) {
        this.id = id;
        this.name = name;
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

    public static ArrayList<Patika> getList(){
        ArrayList<Patika> patikaList = new ArrayList<>();
        String query = "SELECT * FROM patika";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Patika obj;
            while(rs.next()){
                obj = new Patika(rs.getInt("id"),rs.getString("name"));

                patikaList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patikaList;
    }
    public static ArrayList<Patika> getList(int id){
        ArrayList<Patika> patikaList = new ArrayList<>();
        String query = "SELECT * FROM patika INNER JOIN course ON patika.id = course.patika_id";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Patika obj;
            while(rs.next()){
                if(rs.getInt("user_id") == id){
                    obj = new Patika(rs.getInt("id"),rs.getString("name"));
                    patikaList.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patikaList;
    }
    public static ArrayList<Patika> getListForStudent(int user_id){
        ArrayList<Patika> patikaList = new ArrayList<>();
        String query = "SELECT * FROM student INNER JOIN patika ON patika.id = student.patika_id";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Patika obj;
            while(rs.next()){
                if(rs.getInt("user_id") == user_id){
                    obj = new Patika(rs.getInt("id"),rs.getString("name"));
                    patikaList.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patikaList;
    }
    public static ArrayList<Patika> getListRegisterPatika(int user_id){
        ArrayList<Patika> patikaList = new ArrayList<>();
            String query = "SELECT patika.id,patika.name FROM student INNER JOIN patika ON patika.id = student.patika_id WHERE user_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);
            ResultSet rs = pr.executeQuery();

            Patika obj;
            while(rs.next()){
                obj = new Patika(rs.getInt("id"),rs.getString("name"));

                patikaList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patikaList;
    }

    public static boolean add(String name){
        String query = "INSERT INTO patika (name) VALUES (?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static boolean add(String patika_name,int student_id){
        String query = "INSERT INTO student (patika_name) VALUES (?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,patika_name);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static boolean update(int id, String name) {
        String query = "UPDATE patika SET name = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setInt(2, id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static Patika getFetch(int id){
        Patika obj = null;
        String query = "SELECT * FROM patika WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                obj = new Patika(rs.getInt("id"),rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return obj;
    }
    public static int getFetchForStudent(int user_id, int patika_id){
        int stuList = -1;
        String query = "SELECT * FROM student INNER JOIN patika ON patika.id = student.patika_id WHERE user_id = ? AND patika_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);
            pr.setInt(2,patika_id);
            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                stuList = rs.getInt("patika_id");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stuList;
    }
    public static boolean delete(int id){
        String query = "DELETE FROM patika WHERE id=?";
        ArrayList<Course> courseList = Course.getList();

        for(Course obj : courseList){
            if(obj.getPatika().getId() == id){
                Course.delete(obj.getId());
            }
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);

            return pr.executeUpdate() !=-1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static int getIDForStudent(int student_id, String patika_name){
        String query = "SELECT patika.name, patika.id FROM student INNER JOIN patika ON patika.id = student.patika_id WHERE user_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,student_id);
            ResultSet rs = pr.executeQuery();

            while (rs.next()){
                if(Objects.equals(rs.getString("name"), patika_name)){
                    return Integer.parseInt(rs.getString("id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static boolean deleteForStudent(int id){
        String query = "DELETE FROM student WHERE patika_id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
