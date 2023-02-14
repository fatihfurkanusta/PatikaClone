package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;

public class CreateStudentGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_cr_user_name;
    private JTextField fld_cr_user_uname;
    private JButton btn_cr_user_add;
    private JPasswordField fld_cr_user_pass2;
    private JPasswordField fld_cr_user_pass1;

    public CreateStudentGUI(){
        add(wrapper);
        setSize(400,350);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_cr_user_add.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_cr_user_name) || Helper.isFieldEmpty(fld_cr_user_uname) || Helper.isFieldEmpty(fld_cr_user_pass1) || Helper.isFieldEmpty(fld_cr_user_pass2)){
                Helper.showMessage("fill");
            }else{
                String pass1 = Arrays.toString(fld_cr_user_pass1.getPassword());
                String pass2 = Arrays.toString(fld_cr_user_pass2.getPassword());
                if(Objects.equals(pass1,pass2)){
                    if(User.add(fld_cr_user_name.getText(),fld_cr_user_uname.getText(),fld_cr_user_pass1.getText(),"student")){
                        Helper.showMessage("done");
                        dispose();
                    }
                }else{
                    Helper.showMessage("Girdiğiniz parolalar eşleşmiyor.");
                    fld_cr_user_pass2.setText(null);
                    fld_cr_user_pass1.setText(null);
                }
            }
        });
    }
}
