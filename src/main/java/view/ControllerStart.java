package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerStart implements Initializable {
	@FXML
	public ProgressBar progressLoad;

	@FXML
	public AnchorPane anchorPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new SpLashScreen().start();
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
							Logger.getLogger(ControllerStart.class.getName()).log(Level.SEVERE, null, e);
						}
						Stage primaryStage = new Stage();
						Image image = new Image("/images/img_Icon.jpg");
						primaryStage.getIcons().add(image);
						primaryStage.setTitle("Dictionary");
						primaryStage.setScene(new Scene(root));
						primaryStage.show();
						anchorPane.getScene().getWindow().hide();
					}
				});

			} catch (Exception e) {
				System.out.println(""+e);
			}
		}
	}
}
