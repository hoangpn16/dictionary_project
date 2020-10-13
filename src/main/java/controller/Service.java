package controller;

import com.sun.speech.freetts.VoiceManager;
import entity.Word;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import view.Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service {
    Connection con;
    public static Service instance;

    public Service(Connection con) {
        this.con = con;
    }

    public Service() {
    }

    public static Service gI() {
        if (Service.instance == null) {
            Service.instance = new Service();
        }
        return Service.instance;
    }

    public boolean checkWordInData(String word_target) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'");
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public List<Word> searchWord(String word_target) {
        List<Word> listWords = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'");
            while (resultSet.next()) {
                Word word = new Word(resultSet.getString(2), resultSet.getString(3));
                listWords.add(word);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return listWords;
    }

    public List<Word> findByCharacter(String keyword) {
        List<Word> listWordTarget = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM `dictionary` WHERE `word` LIKE '" + keyword + "%' LIMIT 20";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Word rs = new Word(resultSet.getString(2), resultSet.getString(3));
                if (checkItemList(listWordTarget, rs)) {
                    listWordTarget.add(rs);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return listWordTarget;
    }

    public boolean checkItemList(List<Word> listWords, Word word) {
        if (listWords.size() == 0) {
            return true;
        }
        for (int i = 0; i < listWords.size(); i++) {
            if (listWords.get(i).getWord_target().equals(word.getWord_target())
                    && listWords.get(i).getWord_explain().equals(word.getWord_explain())) {
                return false;
            }
        }
        return true;
    }

    public void speech(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(text);
        syntheticVoice.deallocate();
    }

    public void startMsgBox(String info, int idAction, int type, Word word) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(info);

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        if (type == 0) {
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        } else {
            alert.getButtonTypes().setAll(buttonTypeOK);
        }

        Optional<ButtonType> rs = alert.showAndWait();

        if (rs.get() == buttonTypeYes) {
            System.out.println("Yes");
            actionPerform(idAction, word);
        } else if (rs.get() == buttonTypeNo) {
            System.out.println("No");
        } else {
            System.out.println("OK");
        }
    }

    private void actionPerform(int idAction, Word word) {
        switch (idAction) {
            case 1:
                Controller.service.insertWord(word.getWord_target(), word.getWord_explain());
                break;
            case 2:
                Controller.service.fixWord(word.getWord_target(), word.getWord_explain());
                break;
            case 3:
                Controller.service.removeWord(word.getWord_target());
                break;
            default:
                System.out.println("Not action!");
                break;
        }
    }

    public void insertWord(String word_target, String word_explain) {
        try {
            Statement statement = con.createStatement();
            String sql = "INSERT INTO `dictionary`(`word`,`detail`) VALUES ('" + word_target + "','" + word_explain + "') ";
            statement.execute(sql);
            Service.gI().startMsgBox("Đã thêm vào từ điển!", 0, 1, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeWord(String word_target) {
        try {
            Statement statement = con.createStatement();
            statement.executeQuery("SET SQL_SAFE_UPDATES=0 ");
            String query = "DELETE FROM `btl_dictionary`.`diction` WHERE `word` ='" + word_target + "'";
            statement.executeUpdate(query);
            Service.gI().startMsgBox("Đã xóa khỏi từ điển!", 0, 1, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fixWord(String word_target, String detail) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("UPDATE `dictionary` SET `detail` ='" + detail + "' WHERE `word`='" + word_target + "'");
            Service.gI().startMsgBox("Đã sửa vào từ điển!", 0, 1, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
