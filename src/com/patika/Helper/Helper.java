package com.patika.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {
    public static void setLayout(){
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if("Nimbus".equals(info.getName())){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                         UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public static int screenCenter(String axis, Dimension size){
        int point;

        switch (axis){
            case "x":
                point=(Toolkit.getDefaultToolkit().getScreenSize().width- size.width)/2;
                break;
            case "y":
                point=(Toolkit.getDefaultToolkit().getScreenSize().height- size.height)/2;
                break;
            default:
                point=0;
        }
        return point;
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }
    public static boolean isFieldEmpty(JTextArea field){
        return field.getText().trim().isEmpty();
    }
    public static void showMessage(String str){
        optionPageTr();
        String msg;
        String title;
        switch (str){
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz.";
                title = "Hata";
                break;
            case "done":
                msg = "İşlem Başarılı";
                title = "Sonuç";
                break;
            case "error":
                msg = "Bir Hata Oluştu.";
                title = "Hata";
                break;
            default:
                msg = str;
                title = "Mesaj!";
        }
        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.INFORMATION_MESSAGE);
    }
    public static void optionPageTr(){
        UIManager.put("OptionPane.okButtonText","Tamam");
        UIManager.put("OptionPane.yesButtonText","Evet");
        UIManager.put("OptionPane.noButtonText","Hayır");
    }

    public static boolean confirm(String str) {
        optionPageTr();
        String msg;

        switch (str){
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz?";
                break;
            default:
                msg = str;
        }
        return JOptionPane.showConfirmDialog(null, msg,"Son kararın mı?",JOptionPane.YES_NO_OPTION) == 0;
    }

}
