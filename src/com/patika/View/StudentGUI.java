package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Helper.Item;
import com.patika.Model.Content;
import com.patika.Model.Patika;
import com.patika.Model.Quiz;
import com.patika.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JPanel pnl_top;
    private JPanel pnl_bottom;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tab_patika;
    private JTable tbl_patika_list;
    private JTable tbl_reg_patika_list;
    private JPanel pnl_content_list;
    private JTable tbl_content_list;
    private JTextField fld_del_patika_name;
    private JButton btn_del_patika;
    private JComboBox cmb_reg_patika;
    private JPanel pnl_reg_patika;
    private JPanel pnl_patika;
    private JPanel pnl_content;
    private JPanel tab_question;
    private JComboBox cmb_quiz_patika_list;
    private JComboBox cmb_quiz_content_list;
    private JButton btn_quiz_start;
    private JPanel pnl_test;
    private JRadioButton rd_btn1;
    private JLabel lbl_question;
    private JButton btn_finish;
    private JButton btn_next;
    private JButton btn_back;
    private JButton btn_pass;
    private JRadioButton rd_btn2;
    private JRadioButton rd_btn3;
    private JRadioButton rd_btn4;
    private JPanel pnl_buttons;
    private JPanel pnl_counter;
    //private JLabel lbl_counter;
    private JPanel pnl_content_listt;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_reg_patika_list;
    private Object[] row_reg_patika_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private int questionNumber;
    private ButtonGroup buttonGroup;
    private String[] answer;
    private String[] answerSelected;
    private int quizArrayIndex = 0;
    private ArrayList<Quiz> quizArrayList;



    public StudentGUI(Student student) {
        add(wrapper);
        setSize(1000,450);
        setLocation(Helper.screenCenter("x",getSize()),Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        //setResizable(false);
        setVisible(true);

        quizPaneOpenClose(false);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rd_btn1);
        buttonGroup.add(rd_btn2);
        buttonGroup.add(rd_btn3);
        buttonGroup.add(rd_btn4);

        // tab_patika
        lbl_welcome.setText("Hoşgeldin : "+student.getName());

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        // Patika List
        patikaMenu = new JPopupMenu();
        JMenuItem registerMenu = new JMenuItem("Kayıt Ol");
        patikaMenu.add(registerMenu);

        registerMenu.addActionListener(e->{
            int patika_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());

            if(Patika.getFetchForStudent(student.getId(),patika_id) == patika_id){
                Helper.showMessage("Bu Eğitime Zaten Kayıtlısınız.");
            }else{
                Student.add(patika_id, student.getId());
                loadRegisterPatikaModel(student.getId());
                loadRegisterPatikaCombo(student.getId());
                loadQuizPatikaCombo(student.getId());
            }

        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);

        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_patika_list.getColumnModel().getColumn(0).setMinWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int select_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(select_row,select_row);
            }
        });
        // ##Patika List

        // Register Patika
        mdl_reg_patika_list = new DefaultTableModel();
        Object[] col_reg_patika_list = {""};
        mdl_reg_patika_list.setColumnIdentifiers(col_reg_patika_list);

        row_reg_patika_list = new Object[col_patika_list.length];
        loadRegisterPatikaModel(student.getId());

        tbl_reg_patika_list.setModel(mdl_reg_patika_list);
        tbl_reg_patika_list.getTableHeader().setReorderingAllowed(false);
        // ## Register Patika

        tbl_reg_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    Point point = e.getPoint();
                    int select_row = tbl_reg_patika_list.rowAtPoint(point);
                    tbl_reg_patika_list.setRowSelectionInterval(select_row,select_row);

                    String select_patika_name = tbl_reg_patika_list.getValueAt(tbl_reg_patika_list.getSelectedRow(), 0).toString();
                    fld_del_patika_name.setText(select_patika_name);
                }catch (Exception exception){
                }
            }
        });
        btn_del_patika.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_del_patika_name)){
                Helper.showMessage("Lütfen kayıtlı olduğunuz bir patika seçiniz.");
            }else{
                int select_patika_id = Patika.getIDForStudent(student.getId(),fld_del_patika_name.getText());

                if(Patika.deleteForStudent(select_patika_id)){
                    Helper.showMessage("done");
                    loadRegisterPatikaModel(student.getId());
                    fld_del_patika_name.setText(null);
                    loadRegisterPatikaCombo(student.getId());
                    loadQuizPatikaCombo(student.getId());

                }else{
                    Helper.showMessage("error");
                }
            }
        });
        // ## TAB_PATİKA

        // TAB_CONTENT
        // Content List
        mdl_content_list = new DefaultTableModel();
        Object[] col_content_list = {"ID","İçerik Başlığı", "İçerik Açıklaması", "Youtube Linki"};
        mdl_content_list.setColumnIdentifiers(col_content_list);

        row_content_list = new Object[col_content_list.length];

        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_content_list.getColumnModel().getColumn(0).setMinWidth(75);
        loadContentModel(student.getId(), "Tümü");
        loadRegisterPatikaCombo(student.getId());
        loadQuizPatikaCombo(student.getId());
        // ##Content List

        // ## TAB_CONTENT
        cmb_reg_patika.addActionListener(e -> {
            Item selectCombo = (Item) cmb_reg_patika.getSelectedItem();
            try{
                loadContentModel(student.getId(), selectCombo.toString());
            }catch (Exception exception){}
        });

        tab_patika.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    Item contentHead = (Item) cmb_quiz_patika_list.getSelectedItem();
                    loadQuizContentCombo(student.getId(), contentHead.toString());
                }catch (Exception exception){}
            }
        });
        cmb_quiz_patika_list.addActionListener(e -> {
            try{
                Item contentHead = (Item) cmb_quiz_patika_list.getSelectedItem();
                loadQuizContentCombo(student.getId(), contentHead.toString());
            }catch (Exception exception){}

        });
        btn_quiz_start.addActionListener(e -> {
            Item patikaName = (Item) cmb_quiz_patika_list.getSelectedItem();
            Item contentHead = (Item) cmb_quiz_content_list.getSelectedItem();
            if(patikaName == null || contentHead == null){
                Helper.showMessage("Bir pakita ve bir içerik seçmiş olmalısınız.");
            }else{
                quizPaneOpenClose(true);
                loadQuizPane(contentHead.toString());
                answer = new String[quizArrayList.size()];
                answerSelected = new String[quizArrayList.size()];
                //lbl_counter.setText(quizArrayIndex + 1 + " / " + quizArrayList.size());
            }


        });
        btn_finish.addActionListener(e -> {
            quizPaneOpenClose(false);
        });

        btn_next.addActionListener(e -> {
            buttonGroup.clearSelection();
            if(quizArrayIndex >= 0){
                btn_back.setVisible(true);
            }
            if((quizArrayList.size()-1) == quizArrayIndex){
                btn_next.setVisible(false);
            }else{
                Item contentHead = (Item) cmb_quiz_content_list.getSelectedItem();
                quizArrayIndex++;
                quizPut(quizArrayList.get(quizArrayIndex));
            }
            //lbl_counter.setText(quizArrayIndex + 1 + " / " + quizArrayList.size());
            getAnswerSelected();
        });

        btn_back.addActionListener(e -> {
            buttonGroup.clearSelection();
            if(quizArrayIndex != quizArrayList.size()){
                btn_next.setVisible(true);
            }
            if(quizArrayIndex == 0){
                btn_back.setVisible(false);
            }
            else{
                quizArrayIndex--;
                quizPut(quizArrayList.get(quizArrayIndex));
            }
            //lbl_counter.setText(quizArrayIndex + 1 + " / " + quizArrayList.size());
            getAnswerSelected();
        });

        btn_pass.addActionListener(e -> {
            buttonGroup.clearSelection();
            answer[quizArrayIndex] = null;
            answerSelected[quizArrayIndex] = null;
        });

        btn_finish.addActionListener(e -> {
            if(Helper.confirm("sure")){
                quizPaneOpenClose(false);
                answerCalculate();
            }
            quizArrayIndex = 0;
            buttonGroup.clearSelection();
        });
        rd_btn1.addActionListener(e -> {
            answer[quizArrayIndex] = rd_btn1.getActionCommand();
            answerSelected[quizArrayIndex] = "1";
        });
        rd_btn2.addActionListener(e -> {
            answer[quizArrayIndex] = rd_btn2.getActionCommand().toString();
            answerSelected[quizArrayIndex] = "2";
        });
        rd_btn3.addActionListener(e -> {
            answer[quizArrayIndex] = rd_btn3.getActionCommand();
            answerSelected[quizArrayIndex] = "3";
        });
        rd_btn4.addActionListener(e -> {
            answer[quizArrayIndex] = rd_btn4.getActionCommand();
            answerSelected[quizArrayIndex] = "4";
        });
    }
    public void answerCalculate(){
        int t = 0 ;
        for(int i = 0 ; i < answer.length ; i++){
            if(answer[i] != null) {
                if (answer[i].equals("t")) {
                    t++;
                }
                answer[i] = null;
                answerSelected[i] = null;
            }
        }
        int scor = (100 / quizArrayList.size()) * t ;
        Helper.showMessage("Başarı oranınız % " + scor);
    }
    public void getAnswerSelected(){
        if(answerSelected[quizArrayIndex] != null){
            if(answerSelected[quizArrayIndex].equals("1")){
                rd_btn1.setSelected(true);
            }
            else if(answerSelected[quizArrayIndex].equals("2")){
                rd_btn2.setSelected(true);
            }
            else if(answerSelected[quizArrayIndex].equals("3")){
                rd_btn3.setSelected(true);
            }
            else if(answerSelected[quizArrayIndex].equals("4")){
                rd_btn4.setSelected(true);
            }
        }
    }
    public boolean quizPut(Quiz quiz){
        if(quiz == null){
            return false;
        }
        else{
            ArrayList<String> answers = new ArrayList<>();
            answers.add(quiz.getAns_true());
            answers.add(quiz.getAns1());
            answers.add(quiz.getAns2());
            answers.add(quiz.getAns3());

            Collections.shuffle(answers);

            lbl_question.setText(quizArrayIndex + 1+"-) "+quiz.getQuestion());

            rd_btn1.setText(answers.get(0));
            if(answers.get(0) == quiz.getAns_true()){
                rd_btn1.setActionCommand("t");
            }else{
                rd_btn1.setActionCommand("f");
            }
            rd_btn2.setText(answers.get(1));
            if(answers.get(1) == quiz.getAns_true()){
                rd_btn2.setActionCommand("t");
            }else{
                rd_btn2.setActionCommand("f");
            }
            rd_btn3.setText(answers.get(2));
            if(answers.get(2) == quiz.getAns_true()){
                rd_btn3.setActionCommand("t");
            }else{
                rd_btn3.setActionCommand("f");
            }
            rd_btn4.setText(answers.get(3));
            if(answers.get(3) == quiz.getAns_true()){
                rd_btn4.setActionCommand("t");
            }else{
                rd_btn4.setActionCommand("f");
            }
        }
        return true;
    }
    public void quizPaneOpenClose(boolean a){
        pnl_test.setVisible(a);
        pnl_buttons.setVisible(a);
        pnl_counter.setVisible(a);
        btn_next.setVisible(a);
        btn_back.setVisible(a);
    }

    public void loadQuizPane(String contentHead){
        quizArrayList = Quiz.getFetch(contentHead);
        try{
            if(quizArrayList.isEmpty()){
                quizPaneOpenClose(false);
                Helper.showMessage("Bu içeriğe ait test bulunmamaktadır.");
            }else{
                    quizPut(quizArrayList.get(quizArrayIndex));
            }
        }catch (Exception exception){}
    }

    private void loadContentModel(int user_id, String patika_name) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);

        int i;
        if(patika_name.equals("Tümü")){
            for(Content obj : Content.getListRegisterAll(user_id)){
                i=0;
                row_content_list[i++] = obj.getId();
                row_content_list[i++] = obj.getHead();
                row_content_list[i++] = obj.getDescription();
                row_content_list[i++] = obj.getLink();

                mdl_content_list.addRow(row_content_list);
            }
        }else{
            for(Content obj : Content.getListRegister(user_id,patika_name)){
                i=0;
                row_content_list[i++] = obj.getId();
                row_content_list[i++] = obj.getHead();
                row_content_list[i++] = obj.getDescription();
                row_content_list[i++] = obj.getLink();

                mdl_content_list.addRow(row_content_list);
            }
        }
    }

    public void loadQuizContentCombo(int user_id, String patika_name){
        cmb_quiz_content_list.removeAllItems();
        for(Content obj : Content.getListRegister(user_id,patika_name)){
            cmb_quiz_content_list.addItem(new Item(obj.getId(),obj.getHead()));
        }
    }

    public void loadQuizPatikaCombo(int user_id){
        cmb_quiz_patika_list.removeAllItems();
        for(Patika obj : Patika.getListRegisterPatika(user_id)){
            cmb_quiz_patika_list.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    public void loadRegisterPatikaCombo(int user_id){
        cmb_reg_patika.removeAllItems();
        cmb_reg_patika.addItem(new Item(0,"Tümü"));

        for(Patika obj : Patika.getListRegisterPatika(user_id)){
            cmb_reg_patika.addItem(new Item(obj.getId(),obj.getName()));
        }
    }


    private void loadRegisterPatikaModel(int user_id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_reg_patika_list.getModel();
        clearModel.setRowCount(0);

        for(Patika obj : Patika.getListForStudent(user_id)){
            row_reg_patika_list[0] = obj.getName();

            mdl_reg_patika_list.addRow(row_reg_patika_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Patika obj : Patika.getList()){
            i=0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();

            mdl_patika_list.addRow(row_patika_list);
        }
    }
}
