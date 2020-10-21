package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadForm implements Initializable {
    @FXML
    public ProgressBar progressLoad;

    @FXML
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Images.loadImage();
        new SpLashScreen().start();
        Service.gI().loadMySQL();
    }

    /**
     * Hàm load sang form từ điển.
     */
    class SpLashScreen extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 1; i <= 100; i++) {
                    progressLoad.setProgress(0.01 * i);
                    Thread.sleep(12);
                }
                Thread.sleep(100);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
                        } catch (IOException e) {
                            Logger.getLogger(LoadForm.class.getName()).log(Level.SEVERE, null, e);
                        }
                        Controller.primaryStage = new Stage();
                        Controller.primaryStage.getIcons().add(Images.img_icon);
                        Controller.primaryStage.setTitle("Dictionary");
                        Controller.primaryStage.setScene(new Scene(root));
                        Controller.primaryStage.show();
                        anchorPane.getScene().getWindow().hide();
                    }
                });

            } catch (Exception e) {
                System.out.println("" + e);
            }
        }
    }
}
