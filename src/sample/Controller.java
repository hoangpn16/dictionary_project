package sample;

import controller.Service;
import entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.ServerSocket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Circle circle_imgSearch;

    @FXML
    private Circle circle_imgSound;

    @FXML
    private Circle circle_imgAdd;

    @FXML
    private Circle circle_imgCorrect;

    @FXML
    private Circle circle_imgDelete;

    @FXML
    public Button btn_sound;

    @FXML
    public Button btn_add;

    @FXML
    public Button btn_Correct;

    @FXML
    public Button btn_delete;

    @FXML
    public TextField txtWord;

    @FXML
    public ListView listWord = new ListView<>();

    @FXML
    public TextArea txtExplain;

    List<Word> wordList = new ArrayList<>();
    Connection connection = null;
    public static Service service = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMySQL();
        setImgButton();
    }


    private void loadMySQL() {
        System.out.println("-------- MySQL JDBC Connection Demo ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        System.out.println("MySQL JDBC Driver Registered!");
//        Connection connection = null;
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/btl_dictionary?characterEncoding=UTF-8&autoReconnect"
                    + "=true&connectTimeout=30000&socketTimeout=30000&serverTimezone=UTC";
            connection = DriverManager
                    .getConnection(url, "root", "17072000");
            System.out.println("SQL Connection to database established!");
            service = new Service(connection);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println(e);
            return;
        }
    }

    private void setImgButton() {
        Image image = new Image("/sample/img_search.jpg");
        circle_imgSearch.setFill(new ImagePattern(image));
        image = new Image("/sample/img_loa.jpg");
        circle_imgSound.setFill(new ImagePattern(image));
        image = new Image("/sample/img_them.jpg");
        circle_imgAdd.setFill(new ImagePattern(image));
        image = new Image("/sample/img_sua.jpg");
        circle_imgCorrect.setFill(new ImagePattern(image));
        image = new Image("/sample/img_xoa.jpg");
        circle_imgDelete.setFill(new ImagePattern(image));
    }

    public void txtWord_onKeyReleased() {
        actionSearch();
    }

    public void listWord_onMouseClick() {
        if (listWord.getItems().size() == 0) {
            return;
        }
        int index = listWord.getSelectionModel().getSelectedIndex();
        txtExplain.setText(wordList.get(index).toString());
        txtWord.setText(wordList.get(index).getWord_target().toString());
    }

    public void actionSearch() {
        if (txtWord.getText().equals("")) {
            listWord.getItems().clear();
            listWord.setMaxHeight(0);
            txtExplain.setText("");
            return;
        }
        wordList.clear();
        listWord.getItems().clear();
        wordList = service.findByCharacter(txtWord.getText());
        double height = wordList.size() * 24 + 1;
        if (height > 403) {
            height = 403;
        }
        listWord.setMaxHeight(height);
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            listWord.getItems().add(word.getWord_target());
            if (i == 0 && word.getWord_target().equals(txtWord.getText())) {
                txtExplain.setText(wordList.get(i).toString());
            }
        }
        if (listWord.getItems().size() == 0) {
            txtExplain.setText("");
        }
    }

    public void btn_sound_click() {
        Service.gI().speech(txtWord.getText());
    }

    public void btn_add_click() {
        if (txtWord.getText().equals("")) {
            Service.gI().startMsgBox("Vui lòng nhập từ!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        if (service.checkWordInData(txtWord.getText())) {
            Service.gI().startMsgBox("Dữ liệu đã tồn tại!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        if (txtExplain.getText().equals("")) {
            Service.gI().startMsgBox("Vui lòng nhập đầy đủ dữ liệu của từ!", 0, 1, null);
            txtExplain.requestFocus();
            return;
        }
        Word word = new Word(txtWord.getText(), txtExplain.getText());
        Service.gI().startMsgBox("Bạn có chắc chắn là thêm vào từ điển không?", 1, 0, word);
    }

    public void btn_Correct_click() {
        if (txtWord.getText().equals("")) {
            Service.gI().startMsgBox("Vui lòng nhập từ!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        if (!service.checkWordInData(txtWord.getText())) {
            Service.gI().startMsgBox("Dữ liệu chưa tồn tại!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        if (txtExplain.getText().equals("")) {
            Service.gI().startMsgBox("Vui lòng nhập đầy đủ dữ liệu của từ!", 0, 1, null);
            txtExplain.requestFocus();
            return;
        }
        Word word = new Word(txtWord.getText(), txtExplain.getText());
        Service.gI().startMsgBox("Bạn có chắc chắn là sửa vào từ điển không?", 2, 0, word);
    }

    public void btn_delete_click() {
        if (txtWord.getText().equals("")) {
            Service.gI().startMsgBox("Vui lòng nhập từ!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        if (!service.checkWordInData(txtWord.getText())) {
            Service.gI().startMsgBox("Dữ liệu chưa tồn tại!", 0, 1, null);
            txtWord.requestFocus();
            return;
        }
        Word word = new Word(txtWord.getText(), txtExplain.getText());
        Service.gI().startMsgBox("Bạn có chắc chắn là xóa ra khỏi từ điển không?", 3, 0, word);
    }
}
