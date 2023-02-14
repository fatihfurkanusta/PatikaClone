package com.patika.Model;

import com.patika.Helper.DBConnector;
import org.postgresql.jdbc2.ArrayAssistant;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String head;
    private String description;
    private String link;
    private Patika patika;

    public Content(){}
    public Content(int id, String head, String description, String link) {
        this.id=id;
        this.head = head;
        this.description = description;
        this.link = link;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM content WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String searchQuery(String head, int patika_id) {
        String query = "SELECT * FROM content WHERE head ILIKE '%{{head}}%' AND patika_id = {{patika_id}} ";
        query = query.replace("{{head}}",head);
        query = query.replace("{{patika_id}}",Integer.toString(patika_id));
        return query;
    }

    public static ArrayList<Content> searchContentList(String query) {
        ArrayList<Content> searchList = new ArrayList<>();
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Content obj;
            while(rs.next()){
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setHead(rs.getString("head"));
                obj.setDescription(rs.getString("description"));
                obj.setLink(rs.getString("link"));

                searchList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchList;
    }

    public static boolean update(int content_id, String head, String description, String link) {
        String query = "UPDATE content SET head = ?, description = ?, link = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,head);
            pr.setString(2,description);
            pr.setString(3,link);
            pr.setInt(4,content_id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public static boolean add(String head, String description,String link, int user_id,int patika_id) {
        String query = "INSERT INTO content (head, description, link, user_id, patika_id) VALUES (?,?,?,?,?) ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,head);
            pr.setString(2,description);
            pr.setString(3,link);
            pr.setInt(4,user_id);
            pr.setInt(5,patika_id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static ArrayList<Content> getList(int id,int patika_id) {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT * FROM content INNER JOIN course ON content.user_id = course.user_id WHERE content.patika_id = ? AND course.patika_id = ? AND content.user_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,patika_id);
            pr.setInt(2,patika_id);
            pr.setInt(3,id);
            ResultSet rs = pr.executeQuery();

            Content obj;
            while(rs.next()){
                if(rs.getInt("user_id") == id && rs.getInt("patika_id") == patika_id){
                    obj = new Content(rs.getInt("id"),rs.getString("head"), rs.getString("description"), rs.getString("link"));
                    contentList.add(obj);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return contentList;
    }
    public static ArrayList<Content> getListAll(int id) {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT * FROM content";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Content obj;
            while(rs.next()){
                if(rs.getInt("user_id") == id){
                    obj = new Content(rs.getInt("id"),rs.getString("head"), rs.getString("description"), rs.getString("link"));
                    contentList.add(obj);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }
    public static ArrayList<Content> getListRegister(int user_id, String patika_name) {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT content.id,content.head,content.description, content.link, student.user_id, patika.name FROM content INNER JOIN patika ON patika.id = content.patika_id INNER JOIN student ON patika.id = student.patika_id WHERE patika.name = ? AND student.user_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,patika_name);
            pr.setInt(2,user_id);

            ResultSet rs = pr.executeQuery();

            Content obj;
            while(rs.next()){
                obj = new Content(rs.getInt("id"),rs.getString("head"), rs.getString("description"), rs.getString("link"));
                contentList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }
    public static ArrayList<Content> getListRegisterAll(int user_id) {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT content.id,content.head,content.description, content.link, student.user_id, patika.name FROM content INNER JOIN patika ON patika.id = content.patika_id INNER JOIN student ON patika.id = student.patika_id WHERE student.user_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,user_id);

            ResultSet rs = pr.executeQuery();

            Content obj;
            while(rs.next()){
                obj = new Content(rs.getInt("id"),rs.getString("head"), rs.getString("description"), rs.getString("link"));
                contentList.add(obj);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }

}
