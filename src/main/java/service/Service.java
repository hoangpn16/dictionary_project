package service;

import com.sun.speech.freetts.VoiceManager;
import entity.Word;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    public Connection connection;

    public static Service instance;

    public Service(Connection connection) {
        this.connection = connection;
    }

    public Service() {
    }

    public static Service gI() {
        if (Service.instance == null) {
            Service.instance = new Service();
        }
        return Service.instance;
    }

    /**
     * Hàm kiểm tra xem từ đã có trong data chưa.
     * chú ý pass My SQL
     *
     * @param word_target từ tiếng anh
     * @return true or false
     */
    public boolean checkWordInData(String word_target) {
        try {
            Statement statement = connection.createStatement();
            String url = "SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'";
            ResultSet resultSet = statement.executeQuery(url);
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("" + e);
        }
        return false;
    }

    /**
     * Hàm kết nối đến MySQL.
     * add dictionary_data.sql, url: @.../lib/dictionary_data.sql
     */
    public void loadMySQL() {
        System.out.println("-------- MySQL JDBC Connection Demo ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        System.out.println("MySQL JDBC Driver Registered!");
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/btl_dictionary?characterEncoding=UTF-8&autoReconnect"
                    + "=true&connectTimeout=30000&socketTimeout=30000&serverTimezone=UTC";
            connection = DriverManager
                    .getConnection(url, "root", "17072000");
            System.out.println("SQL Connection to database established!");
            instance = new Service(connection);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println(e);
            return;
        }
    }

    /**
     * Hàm tìm kiếm từ.
     *
     * @param word_target từ cần tìm kiếm
     * @return list các từ giống từ tìm được
     */
    public List<Word> searchWord(String word_target) {
        List<Word> listWords = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String url = "SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'";
            ResultSet resultSet = statement.executeQuery(url);
            while (resultSet.next()) {
                Word word = new Word(resultSet.getString(2), resultSet.getString(3));
                listWords.add(word);
            }

        } catch (Exception e) {
            System.out.println("" + e);
        }
        return listWords;
    }

    /**
     * Hàm tìm kiếm từ theo các chữ cái đầu tiên của từ.
     * Giớ hạn 25 từ
     *
     * @param keyword các chữ cái đầu của từ
     * @return list các từ tìm được
     */
    public List<Word> findByCharacter(String keyword) {
        List<Word> listWordTarget = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `dictionary` WHERE `word` LIKE '" + keyword + "%' LIMIT 25";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Word rs = new Word(resultSet.getString(2), resultSet.getString(3));
                if (checkItemList(listWordTarget, rs)) {
                    listWordTarget.add(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("" + e);
        }
        return listWordTarget;
    }

    /**
     * Hàm check từ đã tồn tại trong list chưa.
     *
     * @param listWords List word cần check
     * @param word      từ cần check
     * @return true or false
     */
    private boolean checkItemList(List<Word> listWords, Word word) {
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

    /**
     * Hàm phát âm.
     * add thư viện freetts.jar,  url: @.../lib/freetts-1.2.1-bin/freetts-1.2.1/lib/freetts.jar
     *
     * @param text chữ cần phát âm
     */
    public void speech(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(text);
        syntheticVoice.deallocate();
    }

    /**
     * Hàm hiện thông báo.
     *
     * @param info string
     */
    public void startMsgBox(String info) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(info);
        ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonTypeOK);
        alert.showAndWait();
    }

    /**
     * Hàm thêm từ vào data.
     *
     * @param word_target  từ tiếng anh
     * @param word_explain nghĩa của từ
     */
    public void insertWord(String word_target, String word_explain) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO `dictionary`(`word`,`detail`) VALUES ('"
                    + word_target + "','" + word_explain + "') ";
            statement.execute(sql);
            this.startMsgBox("Đã thêm vào từ điển!");
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

    /**
     * Hàm xóa từ.
     *
     * @param word_target từ cần xóa
     */
    public void removeWord(String word_target) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SET SQL_SAFE_UPDATES=0 ");
            String query = "DELETE FROM `btl_dictionary`.`dictionary` WHERE `word` ='" + word_target + "'";
            statement.executeUpdate(query);
            this.startMsgBox("Đã xóa khỏi từ điển!");
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }

    /**
     * Hàm sửa word.
     *
     * @param word_target  từ tiếng anh
     * @param word_explain nghĩa của từ
     */
    public void fixWord(String word_target, String word_explain) {
        try {
            Statement statement = connection.createStatement();
            String url = "UPDATE `dictionary` SET `detail` ='" + word_explain + "' WHERE `word`='" + word_target + "'";
            statement.executeUpdate(url);
            this.startMsgBox("Đã sửa vào từ điển!");
        } catch (Exception e) {
            System.out.println("" + e);
        }
    }
}
