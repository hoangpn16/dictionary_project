package sample;

import controller.Service;
import entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button btn_Them = new Button();
    public ListView listWord = new ListView<>();
    public TextField txtWord = new TextField();
    public TextArea textArea = new TextArea();
    public Button btn_Search = new Button();

    Service service = new Service();

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("-------- MySQL JDBC Connection Demo ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/btl_dictionary?characterEncoding=UTF-8&autoReconnect=true&connectTimeout=30000&socketTimeout=30000&serverTimezone=UTC", "root", "17072000");
            System.out.println("SQL Connection to database established!");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println(e);
            return;
        }
    }

    public void txtWord_onKeyReleased(){

    }

    public void btn_Search_click(){
        List<Word> wordList = service.searchWord(txtWord.getText());
        listWord.getItems().clear();
        for(Word word: wordList){
            listWord.getItems().add(word.getInfor());
        }
    }
}
