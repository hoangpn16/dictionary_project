package view;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Service;
import entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
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
    public ListView listWord;

    @FXML
    public TextArea txtExplain;

    public static Stage primaryStage;

    List<Word> wordList = new ArrayList<>();

    private int numSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImgButton();
        listWord.setMaxHeight(0);
    }

    /**
     * Hàm delay search.
     * Sau 0.5 không nhập text sẽ search
     */
    class delaySearch extends Thread {
        @Override
        public void run() {
            int num = numSearch;
            for (int i = 0; i < 500; i++) {
                if (num != numSearch) {
                    return;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (num != numSearch) {
                return;
            }
            actionSearch();
        }
    }

    /**
     * Hàm load ảnh cho các button.
     */
    private void setImgButton() {
        circle_imgSearch.setFill(new ImagePattern(Images.img_search));
        circle_imgSound.setFill(new ImagePattern(Images.img_loa));
        circle_imgAdd.setFill(new ImagePattern(Images.img_add));
        circle_imgCorrect.setFill(new ImagePattern(Images.img_sua));
        circle_imgDelete.setFill(new ImagePattern(Images.img_xoa));
    }

    /**
     * Bắt sự kiện nhập phím.
     */
    public void txtWord_onKeyReleased() {
//		this.numSearch++;
//		new delaySearch().start();
        actionSearch();
        btn_sound.setDisable(txtWord.getText().trim().isEmpty());
    }

    /**
     * Bắt sự kiện click vào item của listview.
     * set từ lên txtWord
     * set nghĩa lên txtExplain
     */
    public void listWord_onMouseClick() {
        if (listWord.getItems().size() == 0) {
            return;
        }
        int index = listWord.getSelectionModel().getSelectedIndex();
        txtExplain.setText(wordList.get(index).toString());
        txtWord.setText(wordList.get(index).getWord_target().toString());
    }

    /**
     * Hàm search.
     */
    public void actionSearch() {
        if (txtWord.getText().equals("")) {
            listWord.getItems().clear();
            listWord.setMaxHeight(0);
            txtExplain.setText("");
            return;
        }
        wordList.clear();
        listWord.getItems().clear();
        wordList = Service.gI().findByCharacter(txtWord.getText());
        double height = wordList.size() * 30 + 1;
        if (height > 555) {
            height = 555;
        }
        listWord.setMaxHeight(height);
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            listWord.getItems().add(word.getWord_target());
            if (i == 0 && word.getWord_target().equals(txtWord.getText())) {
                txtExplain.setText(wordList.get(i).toString());
                listWord.getSelectionModel().select(0);
            }
        }
        if (listWord.getItems().size() == 0) {
            txtExplain.setText("");
        }
    }

    /**
     * Bắt sự kiện button phát âm.
     * Phát âm text trên txtWord
     */
    public void btn_sound_click() {
        Service.gI().speech(txtWord.getText());
    }

    /**
     * Bắt sự kiện button add.
     */
    public void btn_add_click() {
        if (!txtWord.getText().equals("") && txtExplain.getText().equals("")) {
            AddItemWord.txtTargetAdd = txtWord.getText();
        } else {
            AddItemWord.txtTargetAdd = "";
        }
        new AddItemWord().actionStart();
    }

    /**
     * Bắt sự kiện button correct.
     */
    public void btn_Correct_click() {
        FixItemWord.txtTarget = txtWord.getText();
        new FixItemWord().actionStart();
    }

    /**
     * Bắt sự kiện button delete.
     */
    public void btn_delete_click() {
        if (!txtWord.getText().equals("") && !txtExplain.getText().equals("")) {
            DeleteItemWord.txtTargetdelete = txtWord.getText();
        } else {
            DeleteItemWord.txtTargetdelete = "";
        }
        new DeleteItemWord().actionStart();
    }

}
