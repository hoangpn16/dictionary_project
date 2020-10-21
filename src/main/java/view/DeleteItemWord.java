package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class DeleteItemWord implements Initializable {
    @FXML
    public Button btnDelete;

    @FXML
    public Button btnCancel;

    @FXML
    public TextField txt_target;

    @FXML
    public Label status;

    public Stage primaryStage;

    @FXML
    public AnchorPane anchorPane;

    private String textCheck = "";

    @FXML
    public Circle images;

    static String txtTargetdelete;

    /**
     * Hàm bắt sự kiện button cancel = thoát.
     */
    public void btnCancel_click() {
        anchorPane.getScene().getWindow().hide();
    }

    /**
     * Bắt sự kiện button xóa.
     */
    public void btnDelete_click() {
        textCheck = txt_target.getText();
        if (!Service.gI().checkWordInData(txt_target.getText())) {
            status.setText("Dữ liệu chưa tồn tại.");
            status.setTextFill(Color.web("#FF0000"));
            images.setFill(new ImagePattern(Images.img_canhbao));
            txt_target.requestFocus();
            return;
        }
        Service.gI().removeWord(txt_target.getText());
        status.setText("Đã xóa khỏi từ điển.");
        status.setTextFill(Color.web("#00FF00"));
        images.setFill(new ImagePattern(Images.img_tichxanh));
    }

    /**
     * Load form.
     */
    public void actionStart() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/formDelete.fxml"));
        } catch (IOException e) {
            Logger.getLogger(LoadForm.class.getName()).log(Level.SEVERE, null, e);
        }
        primaryStage = new Stage();
        primaryStage.getIcons().add(Images.img_icon);
        primaryStage.setTitle("Delete Word");
        primaryStage.setScene(new Scene(root));
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(Controller.primaryStage);
        primaryStage.show();
    }

    /**
     * Bắt sự kiện nhập phím ở textField.
     */
    public void txt_target_onKeyReleased() {
        if (!txt_target.getText().equals(textCheck)) {
            status.setText("");
            textCheck = "";
            images.setFill(new ImagePattern(Images.img_null));
        }
        btnDelete.setDisable(txt_target.getText().trim().isEmpty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        images.setFill(new ImagePattern(Images.img_null));
        if (!txtTargetdelete.equals("")) {
            txt_target.setText(txtTargetdelete);
            btnDelete.setDisable(false);
        }
    }
}
