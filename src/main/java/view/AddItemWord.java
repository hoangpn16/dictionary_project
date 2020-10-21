package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddItemWord implements Initializable {
    @FXML
    public Button btnAdd;

    @FXML
    public Button btnCancel;

    @FXML
    public TextArea txt_explain;

    @FXML
    public TextField txt_target;

    @FXML
    public Label status;

    @FXML
    public Circle images;

    public Stage primaryStage;

    static String txtTargetAdd;

    @FXML
    public AnchorPane anchorPane;

    private String textCheck = "";

    /**
     * Sự kiện button add = check, thêm item.
     */
    public void btnAdd_click() {
        textCheck = txt_target.getText();
        if (Service.gI().checkWordInData(txt_target.getText())) {
            images.setFill(new ImagePattern(Images.img_canhbao));
            status.setText("Dữ liệu đã tồn tại.");
            status.setTextFill(Color.web("#FF0000"));
            txt_target.requestFocus();
            return;
        }
        if (txt_explain.getText().equals("")) {
            status.setText("Vui lòng nhập đầy đủ dữ liệu của từ.");
            status.setTextFill(Color.web("#FF0000"));
            images.setFill(new ImagePattern(Images.img_canhbao));
            txt_explain.requestFocus();
            return;
        }
        Service.gI().insertWord(txt_target.getText(), txt_explain.getText().replaceAll("\n", ""));
        status.setText("Đã thêm vào từ điển.");
        status.setTextFill(Color.web("#00FF00"));
        images.setFill(new ImagePattern(Images.img_tichxanh));
    }

    /**
     * Sụ kiện button cancel = thoát chương trình.
     */
    public void btnCancel_click() {
        anchorPane.getScene().getWindow().hide();
    }

    /**
     * Load form.
     */
    public void actionStart() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/formAdd.fxml"));
        } catch (IOException e) {
            Logger.getLogger(LoadForm.class.getName()).log(Level.SEVERE, null, e);
        }
        primaryStage = new Stage();
        primaryStage.getIcons().add(Images.img_icon);
        primaryStage.setTitle("Insert Word");
        primaryStage.setScene(new Scene(root));
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(Controller.primaryStage);
        primaryStage.show();
    }

    /**
     * Bắt sự kiến nhập phím vào textField.
     */
    public void txt_target_onKeyReleased() {
        if (!txt_target.getText().equals(textCheck)) {
            status.setText("");
            textCheck = "";
            images.setFill(new ImagePattern(Images.img_null));
        }
        btnAdd.setDisable(txt_target.getText().trim().isEmpty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!txtTargetAdd.equals("")) {
            txt_target.setText(txtTargetAdd);
            btnAdd.setDisable(false);
        }
        images.setFill(new ImagePattern(Images.img_null));
    }
}
