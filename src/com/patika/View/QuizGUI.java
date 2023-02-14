package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Model.Quiz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_question;
    private JTextField fld_ansTrue;
    private JTextField fld_ans1;
    private JTextField fld_ans2;
    private JTextField fld_ans3;
    private JButton btn_add_question;

    public QuizGUI(int content_id){
        add(wrapper);
        setSize(300,400);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_add_question.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_question) || Helper.isFieldEmpty(fld_ansTrue) || Helper.isFieldEmpty(fld_ans1) || Helper.isFieldEmpty(fld_ans2) || Helper.isFieldEmpty(fld_ans3)){
                Helper.showMessage("fill");
            }else{
                if(Quiz.add(fld_question.getText(),fld_ansTrue.getText(),fld_ans1.getText(),fld_ans2.getText(),fld_ans3.getText(),content_id)){
                    Helper.showMessage("done");
                    dispose();
                }
            }
        });
    }
}
