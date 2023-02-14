package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Helper.Item;
import com.patika.Model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTable tbl_quiz_list;
    private JTable tbl_content_list;
    private JTextField fld_course_name;
    private JPanel pnl_top;
    private JTabbedPane tab_patikalar;
    private JPanel pnl_user_list;
    private JScrollPane scrl_userList;
    private JPanel pnl_user_form;
    private JLabel lbl_;
    private JTextField fld_content_head;
    private JTextField fld_content_link;
    private JButton btn_user_add;
    private JTextField fld_sh_content_head;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JComboBox cmb_patika_name;
    private JTextArea area_content_description;
    private JTextField fld_del_content_head;
    private User educator;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private JPopupMenu contentMenu;
    private DefaultTableModel mdl_quiz_list;
    private Object[] row_quiz_list;
    private  JPopupMenu quizMenu;

    public EducatorGUI(Educator educator){
        add(wrapper);
        setSize(850,550);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin : "+educator.getName());

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        // Patika List
        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);

        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel(educator.getId());

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_patika_list.getColumnModel().getColumn(0).setMinWidth(75);

        tbl_patika_list.getSelectionModel().addListSelectionListener(e -> {
            try{
                String select_course_name = tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 1).toString();
                fld_course_name.setText(select_course_name);

            }catch(Exception exception){
            }
        });

        // ## Course List

        // Content List
        contentMenu = new JPopupMenu();
        JMenuItem quizAddMenu = new JMenuItem("Quiz Sorusu Ekle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        contentMenu.add(quizAddMenu);
        contentMenu.add(deleteMenu);

        deleteMenu.addActionListener(e->{
            Item patikaName = (Item) cmb_patika_name.getSelectedItem();
            if(Helper.confirm("sure")){
                int select_content_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),0).toString());
                if(Content.delete(select_content_id)){
                    Helper.showMessage("done");
                    loadContentModel(educator.getId(),patikaName.getKey());
                }
            }
        });
        quizAddMenu.addActionListener(e->{
            int content_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),0).toString());
            QuizGUI quiz = new QuizGUI(content_id);
        });


        mdl_content_list = new DefaultTableModel();
        Object[] col_content_list = {"ID","İçerik Başlığı", "İçerik Açıklaması", "Youtube Linki"};
        mdl_content_list.setColumnIdentifiers(col_content_list);

        row_content_list = new Object[col_content_list.length];


        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.setComponentPopupMenu(contentMenu);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo(educator.getId());

        tbl_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_content_list.rowAtPoint(point);
                tbl_content_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        cmb_patika_name.addActionListener(e -> {
            Item patikaName = (Item) cmb_patika_name.getSelectedItem();
            loadContentModel(educator.getId(), patikaName.getKey());
        });

        tbl_content_list.getModel().addTableModelListener(e->{
            if(e.getType() == TableModelEvent.UPDATE){
                int content_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),0).toString());
                String head = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),1).toString();
                String description = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),2).toString();
                String link = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(),3).toString();

                if(Content.update(content_id, head,description,link)){
                    Helper.showMessage("done");
                }else{
                    Helper.showMessage("error");
                }
            }
        });
        // ## Content List

        // Content add
        btn_user_add.addActionListener(e -> {
            Item patikaName = (Item) cmb_patika_name.getSelectedItem();
            if(Helper.isFieldEmpty(fld_content_head) || Helper.isFieldEmpty(fld_content_link) || Helper.isFieldEmpty(area_content_description)){
                Helper.showMessage("fill");
            }else{
                if(Content.add(fld_content_head.getText(), area_content_description.getText(), fld_content_link.getText(), educator.getId() , patikaName.getKey())){
                    Helper.showMessage("done");
                    loadContentModel(educator.getId(), patikaName.getKey());
                    fld_content_head.setText(null);
                    fld_content_link.setText(null);
                    area_content_description.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
        // ## Content add

        // Quiz List
        quizMenu = new JPopupMenu();
        JMenuItem deleteMenuQuiz = new JMenuItem("Sil") ;
        quizMenu.add(deleteMenuQuiz);

        deleteMenuQuiz.addActionListener(e->{
            Item patikaName = (Item) cmb_patika_name.getSelectedItem();
            if(Helper.confirm("sure")){
                int select_quiz_id = Integer.parseInt(tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),0).toString());
                if(Quiz.delete(select_quiz_id)){
                    Helper.showMessage("done");
                    loadQuizModel();
                }
            }
        });

        mdl_quiz_list = new DefaultTableModel();
        Object[] col_quiz_list = {"ID", "Soru", "Doğru Cevap", "Yanlış Cevap", "Yanlış Cevap", "Yanlış Cevap", "Ait Olduğu İçerik"};
        mdl_quiz_list.setColumnIdentifiers(col_quiz_list);

        row_quiz_list = new Object[col_quiz_list.length];

        tbl_quiz_list.setModel(mdl_quiz_list);
        tbl_quiz_list.getTableHeader().setReorderingAllowed(false);
        tbl_quiz_list.setComponentPopupMenu(quizMenu);
        tbl_quiz_list.getColumnModel().getColumn(0).setMaxWidth(75);

        loadQuizModel();

        tbl_quiz_list.getModel().addTableModelListener(e->{
            if(e.getType() == TableModelEvent.UPDATE){
                int quiz_id = Integer.parseInt(tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),0).toString());
                String question = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),1).toString();
                String ans_true = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),2).toString();
                String ans2 = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),3).toString();
                String ans3 = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),3).toString();
                String ans1 = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(),3).toString();

                if(Quiz.update(quiz_id, question, ans_true, ans2, ans3, ans1)){
                    Helper.showMessage("done");
                }else{
                    Helper.showMessage("error");
                }
            }
        });
        tbl_quiz_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_quiz_list.rowAtPoint(point);
                tbl_quiz_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        // ## Quiz List

        btn_user_sh.addActionListener(e -> {
            Item patika = (Item) cmb_patika_name.getSelectedItem();
            String head = fld_sh_content_head.getText();
            String query = Content.searchQuery(head,patika.getKey());
            loadContentModel(Content.searchContentList(query));
        });
        tab_patikalar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadQuizModel();
                //Item patika = (Item) cmb_patika_name.getSelectedItem();
                loadContentModel(educator.getId(),0);
            }
        });

    }

    private void loadContentModel(ArrayList<Content> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);

        int i;
        for (Content obj : list){
            i=0;
            row_content_list[i++] = obj.getId();
            row_content_list[i++] = obj.getHead();
            row_content_list[i++] = obj.getDescription();
            row_content_list[i++] = obj.getLink();

            mdl_content_list.addRow(row_content_list);
        }
    }

    public void loadQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quiz_list.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Quiz obj : Quiz.getList()){
            i=0;
            row_quiz_list[i++] = obj.getId();
            row_quiz_list[i++] = obj.getQuestion();
            row_quiz_list[i++] = obj.getAns_true();
            row_quiz_list[i++] = obj.getAns1();
            row_quiz_list[i++] = obj.getAns2();
            row_quiz_list[i++] = obj.getAns3();
            row_quiz_list[i++] = obj.getContent_head();

            mdl_quiz_list.addRow(row_quiz_list);
        }
    }

    public void loadPatikaCombo(int id){
        cmb_patika_name.removeAllItems();
        cmb_patika_name.addItem(new Item(0,"Tümü"));
        for(Patika obj:Patika.getList(id)){
            cmb_patika_name.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    private void loadContentModel(int id,int patika_id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);

        int i;
        if(patika_id == 0){
            for(Content obj : Content.getListAll(id)){
                i=0;

                row_content_list[i++] = obj.getId();
                row_content_list[i++] = obj.getHead();
                row_content_list[i++] = obj.getDescription();
                row_content_list[i++] = obj.getLink();

                mdl_content_list.addRow(row_content_list);
            }
        }else{
            for(Content obj : Content.getList(id, patika_id)){
                i=0;

                row_content_list[i++] = obj.getId();
                row_content_list[i++] = obj.getHead();
                row_content_list[i++] = obj.getDescription();
                row_content_list[i++] = obj.getLink();

                mdl_content_list.addRow(row_content_list);
            }
        }



    }

    public void loadPatikaModel(int id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Patika obj: Patika.getList(id)){
            i=0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();

            mdl_patika_list.addRow(row_patika_list);
        }
    }

}
