package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Model.Educator;
import com.patika.Model.Operator;
import com.patika.Model.Student;
import com.patika.Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JButton btn_login;
    private JPanel pnl_bottom2;
    private JLabel lbl_create_stu;

    public LoginGUI(){
        add(wrapper);
        setSize(350,400);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)){
                Helper.showMessage("fill");
            }else{
                User user = User.getFetch(fld_user_uname.getText(),fld_user_pass.getText());
                if(user==null){
                    Helper.showMessage("Kullanıcı Adı veya Parolayı Yanlış Girdiniz. !");
                }else{
                    switch (user.getType()){
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) user);
                            break;
                        case "educator":
                            EducatorGUI edGUI =new EducatorGUI((Educator) user);
                            break;
                        case "student":
                            StudentGUI stGUI = new StudentGUI((Student) user);
                            break;
                    }
                    dispose();
                }
            }
        });
        lbl_create_stu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateStudentGUI createStu = new CreateStudentGUI();
            }
        });

        lbl_create_stu.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if((e.getX()<100 && e.getX()>0) && (e.getY()<20 && e.getY()>0)){
                    lbl_create_stu.setForeground(Color.RED);
                }else{
                    lbl_create_stu.setForeground(Color.BLACK);
                }
            }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
