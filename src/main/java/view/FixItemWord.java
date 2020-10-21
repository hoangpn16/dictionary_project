package view;

import entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FixItemWord implements Initializable {
    @FXML
    public Button btnFix;

    @FXML
    public Button btnCheck;

    @FXML
    public Button btnCancel;

    @FXML
    public TextArea txt_explain;

    @FXML
    public TextField txt_target;

    @FXML
    public Circle img_check;

    @FXML
    public Label status;

    public Stage primaryStage;

    public Image img_null;

    @FXML
    public AnchorPane anchorPane;

    private String textCheck = "";

    static String txtTarget;

    /**
     * Bắt sự kiện button Fix.
     */
    public void btnFix_click() {
        textCheck = txt_target.getText();
        Service.gI().fixWord(txt_target.getText(), txt_explain.getText().replaceAll("\n", ""));
        img_check.setFill(new ImagePattern(Images.img_tichxanh));
        status.setText("Sửa thành công.");
        status.setTextFill(Color.web("#00FF00"));
    }

    /**
     * Bắt sự kiện button cancel = thoát.
     */
    public void btnCancel_click() {
        anchorPane.getScene().getWindow().hide();
    }

    /**
     * Bắt sự kiện button check.
     */
    public void btnCheck_click() {
        textCheck = txt_target.getText();
        checked();
    }

    /**
     * Hàm check.
     */
    public void checked() {
        List<Word> list = Service.gI().searchWord(txt_target.getText());
        if (list.size() == 0) {
            img_check.setFill(new ImagePattern(Images.img_canhbao));
            status.setText("Không tồn tại trong từ điển.");
            status.setTextFill(Color.web("#FF0000"));
        } else {
            txt_explain.setText(list.get(0).toString());
            img_check.setFill(new ImagePattern(Images.img_tichxanh));
            status.setText("Có thể sửa.");
            status.setTextFill(Color.web("#00FF00"));
            txt_explain.setDisable(false);
        }
    }

    /**
     * Load form.
     */
    public void actionStart() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/formFix.fxml"));
        } catch (IOException e) {
            Logger.getLogger(LoadForm.class.getName()).log(Level.SEVERE, null, e);
        }
        primaryStage = new Stage();
        primaryStage.getIcons().add(Images.img_icon);
        primaryStage.setTitle("Correct Word");
        primaryStage.setScene(new Scene(root));
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(Controller.primaryStage);
        primaryStage.show();
    }

    /**
     * Bắt sự kiện nhập phím ở textField.
     */
    public void txt_target_onKeyReleased() {
        btnCheck.setDisable(txt_target.getText().trim().isEmpty());
        if (!txt_target.getText().equals(textCheck)) {
            status.setText("");
            textCheck = "";
            img_check.setFill(new ImagePattern(Images.img_null));
            status.setText("");
            txt_explain.setText("");
            txt_explain.setDisable(true);
        }
        boolean flag = false;
        if (txt_explain.getText().equals("")) {
            flag = true;
        }
        btnFix.setDisable(flag);
    }

    /**
     * Bắt sự kiện nhập phím ở TextArea.
     */
    public void txt_explain_onKeyReleased() {
        btnFix.setDisable(txt_explain.getText().trim().isEmpty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        img_check.setFill(new ImagePattern(Images.img_null));
        if (!txtTarget.equals("")) {
            txt_target.setText(txtTarget);
            checked();
        }
    }
}
