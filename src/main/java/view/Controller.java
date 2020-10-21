package view;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.Service;
import entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.*;
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
	private int numSearch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadMySQL();
		setImgButton();
		listWord.setMaxHeight(0);
	}

	/**
	 * Hàm kết nối đến MySQL.
	 * add dictionary_data.sql, url: @.../lib/dictionary_data.sql
	 */
	private void loadMySQL() {
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
			service = new Service(connection);

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			System.out.println(e);
			return;
		}
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
		Image image = new Image("/images/img_search.jpg");
		circle_imgSearch.setFill(new ImagePattern(image));
		image = new Image("/images/img_loa.jpg");
		circle_imgSound.setFill(new ImagePattern(image));
		image = new Image("/images/img_them.jpg");
		circle_imgAdd.setFill(new ImagePattern(image));
		image = new Image("/images/img_sua.jpg");
		circle_imgCorrect.setFill(new ImagePattern(image));
		image = new Image("/images/img_xoa.jpg");
		circle_imgDelete.setFill(new ImagePattern(image));
	}

	/**
	 * Bắt sự kiện nhập phím.
	 */
	public void txtWord_onKeyReleased() {
//		this.numSearch++;
//		new delaySearch().start();
		actionSearch();
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
		wordList = service.findByCharacter(txtWord.getText());
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

	/**
	 * Hàm bắt sự kiện sửa từ.
	 * Kiểm tra text nhập vào
	 */
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

	/**
	 * Bắt sự kiện xóa từ.
	 * Kiểm tra text nhập vào
	 */
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
