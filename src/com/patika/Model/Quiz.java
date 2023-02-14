package com.patika.Model;

import com.patika.Helper.DBConnector;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;

public class Quiz {
    private int id;
    private String question;
    private String ans_true;
    private String ans1;
    private String ans2;
    private String ans3;
    private int content_id;
    private String content_head;

    public Quiz(int id, String question, String ans_true, String ans1, String ans2, String ans3, int content_id, String content_head) {
        this.id = id;
        this.question = question;
        this.ans_true = ans_true;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.content_id = content_id;
        this.content_head = content_head;
    }

    public static ArrayList<Quiz> getList() {
        ArrayList<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM quiz INNER JOIN content ON content.id = quiz.content_id";

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);

            Quiz obj;
            while (rs.next()){
                obj = new Quiz(rs.getInt("id"),rs.getString("question"),rs.getString("ans_true"),rs.getString("ans1"),rs.getString("ans2"),rs.getString("ans3"),rs.getInt("content_id"),rs.getString("head"));
                quizList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizList;
    }

    public static boolean add(String question, String ansTrue, String ans1, String ans2, String ans3, int content_id) {
        String query = "INSERT INTO quiz (question, ans_true, ans2, ans3, ans1, content_id) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,question);
            pr.setString(2,ansTrue);
            pr.setString(3,ans2);
            pr.setString(4,ans3);
            pr.setString(5,ans1);
            pr.setInt(6,content_id);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean update(int quizId, String question, String ans_true, String ans2, String ans3, String ans1) {
        String query = "UPDATE quiz SET question = ?, ans_true = ?, ans1 = ?, ans2 = ?, ans3 = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,question);
            pr.setString(2,ans_true);
            pr.setString(3,ans1);
            pr.setString(4,ans2);
            pr.setString(5,ans3);
            pr.setInt(6,quizId);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM quiz WHERE id=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static String getFetch2(String content_head) {
        String query = "SELECT * FROM quiz INNER JOIN content ON content.id = quiz.content_id WHERE content.head =  ?";
        String question = null;

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,content_head);
            ResultSet rs = pr.executeQuery();


            while(rs.next()){

                question = rs.getString("content_head");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }
    public static ArrayList<Quiz> getFetch(String content_head) {
        String query = "SELECT * FROM quiz INNER JOIN content ON content.id = quiz.content_id WHERE content.head =  ?";
        ArrayList<Quiz> question = new ArrayList<>();

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,content_head);
            ResultSet rs = pr.executeQuery();

            Quiz obj;
            while(rs.next()){
                obj = new Quiz(rs.getInt("id"), rs.getString("question"),rs.getString("ans_true"),rs.getString("ans1"),rs.getString("ans2"),rs.getString("ans3"),-1,"");

                question.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    public String getContent_head() {
        return content_head;
    }

    public void setContent_head(String content_head) {
        this.content_head = content_head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns_true() {
        return ans_true;
    }

    public void setAnsTrue(String ans_true) {
        this.ans_true = ans_true;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int course_id) {
        this.content_id = content_id;
    }
}
