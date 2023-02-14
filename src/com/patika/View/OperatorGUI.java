package com.patika.View;

import com.patika.Helper.Config;
import com.patika.Helper.Helper;
import com.patika.Helper.Item;
import com.patika.Model.Course;
import com.patika.Model.Operator;
import com.patika.Model.Patika;
import com.patika.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;


public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JPanel pnl_top;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_userList;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JLabel lbl_;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JTable tbl_course_list;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JButton btn_course_add;
    private JComboBox cmb_course_educator;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;


    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;

        add(wrapper);
        setSize(1000,500);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin : "+operator.getName());

        // UserList
        mdl_user_list = new DefaultTableModel();
        Object[] col_user_list = {"ID","Ad Soyad","Kullanıcı Adı","Parola","Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];

        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);
        tbl_user_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try{
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);

            }catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        });

        tbl_user_list.getModel().addTableModelListener(e->{
            if(e.getType() == TableModelEvent.UPDATE){
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if(User.update(user_id,user_name,user_uname,user_pass,user_type)){
                    if(!(Objects.equals(user_type, "operator") || Objects.equals(user_type,"educator") || Objects.equals(user_type,"student"))){
                        Helper.showMessage("error");
                    }else{
                        Helper.showMessage("done");
                    }
                }
                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });
        // ## UserList

        // PatikaList
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(),0).toString());
                try{
                    if(Patika.delete(select_id)){

                        Helper.showMessage("done");
                        loadPatikaModel();
                        loadPatikaCombo();
                        loadCourseModel();
                    }else{
                        Helper.showMessage("error");
                    }
                }catch (Exception exception){}

            }

        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_patika_list.getColumnModel().getColumn(0).setMinWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        //## PatikaList

        // CourseList
        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo();
        loadEducatorCombo();
        // ## CourseList
        btn_user_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)){
                    Helper.showMessage("fill");
                }else{
                    String name = fld_user_name.getText();
                    String uname = fld_user_uname.getText();
                    String pass = fld_user_pass.getText();
                    String type = cmb_user_type.getSelectedItem().toString();
                    if(User.add(name,uname,pass,type)){
                        Helper.showMessage("done");
                        loadUserModel();
                        loadEducatorCombo();
                        fld_user_name.setText(null);
                        fld_user_uname.setText(null);
                        fld_user_pass.setText(null);
                    }
                }
            }
        });
        btn_user_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_user_id)) {
                    Helper.showMessage("fill");
                } else {
                    if (Helper.confirm("sure")) {
                        int user_id = Integer.parseInt(fld_user_id.getText());
                        try{
                            if (User.delete(user_id)) {
                                Helper.showMessage("done");
                                loadUserModel();
                                loadEducatorCombo();
                                loadCourseModel();
                                fld_user_id.setText(null);
                            } else {
                                Helper.showMessage("error");
                            }
                        }catch (Exception exception){}

                    }
                }
            }
        });

        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();

            String query = User.searchQuery(name, uname, type);
            loadUserModel(User.searchUserList(query));
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");
            }else{
                if(Patika.add(fld_patika_name.getText())){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_educator.getSelectedItem();
            if(Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)){
                Helper.showMessage("fill");
            }else{
                if(Course.add(userItem.getKey(), patikaItem.getKey(),fld_course_name.getText(), fld_course_lang.getText())){
                    Helper.showMessage("done");
                    loadCourseModel();
                    fld_course_name.setText((null));
                    fld_course_lang.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();

        for(Patika obj:Patika.getList()){
            cmb_course_patika.addItem(new Item(obj.getId(),obj.getName()));
        }
    }
    public void loadEducatorCombo(){
        cmb_course_educator.removeAllItems();
        for(User obj : User.getListOnlyEducator()){
            cmb_course_educator.addItem(new Item(obj.getId(),obj.getName()));
        }
    }

    public void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Course obj : Course.getList()){
            i=0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();

            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Patika obj: Patika.getList()){
            i=0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();

            mdl_patika_list.addRow(row_patika_list);
        }

    }

    public void loadUserModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(User obj : User.getList()){
            i=0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();

            mdl_user_list.addRow(row_user_list);
        }
    }
    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for(User obj : list){
            int i=0;

            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();

            mdl_user_list.addRow(row_user_list);
        }
    }

}
