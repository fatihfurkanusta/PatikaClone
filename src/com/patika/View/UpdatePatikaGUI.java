package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.DBConnector;
import com.patika.Helper.Helper;
import com.patika.Model.Patika;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePatikaGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_patika_name;
    private JButton btn_update;
    private Patika patika;

    public UpdatePatikaGUI(Patika patika){
        this.patika = patika;
        add(wrapper);
        setSize(300,150);
        setLocation(Helper.screenCenter("x",getSize()) , Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        fld_patika_name.setText(patika.getName());

        btn_update.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }else{
                if(Patika.update(patika.getId(),fld_patika_name.getText())){
                    Helper.showMessage("done");
                }
                dispose();
            }

        });
    }
}
