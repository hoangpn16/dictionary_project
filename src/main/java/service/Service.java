package service;

import com.sun.speech.freetts.VoiceManager;
import entity.Word;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import view.Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service {
	Connection con;

	public static Service instance;

	public Service(Connection con) {
		this.con = con;
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
	 *
	 * @param word_target từ tiếng anh
	 * @return true or false
	 */
	public boolean checkWordInData(String word_target) {
		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'");
			if (resultSet.next()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("" + e);
		}
		return false;
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
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'");
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
			Statement statement = con.createStatement();
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
	public boolean checkItemList(List<Word> listWords, Word word) {
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
	 * Hàm hiện bảng thông báo thực thi các action.
	 *
	 * @param info     thông báo
	 * @param idAction id action
	 * @param type     loại thông báo
	 * @param word     word
	 */
	public void startMsgBox(String info, int idAction, int type, Word word) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Notification");
		alert.setHeaderText(info);

		ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
		ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

		if (type == 0) {
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
		} else {
			alert.getButtonTypes().setAll(buttonTypeOK);
		}

		Optional<ButtonType> rs = alert.showAndWait();

		if (rs.get() == buttonTypeYes) {
			System.out.println("Yes");
			actionPerform(idAction, word);
		} else if (rs.get() == buttonTypeNo) {
			System.out.println("No");
		} else {
			System.out.println("OK");
		}
	}

	/**
	 * Hàm action.
	 *
	 * @param idAction int
	 * @param word     word
	 */
	private void actionPerform(int idAction, Word word) {
		switch (idAction) {
			case 1:
				Controller.service.insertWord(word.getWord_target(), word.getWord_explain());
				break;
			case 2:
				Controller.service.fixWord(word.getWord_target(), word.getWord_explain());
				break;
			case 3:
				Controller.service.removeWord(word.getWord_target());
				break;
			default:
				System.out.println("Not action!");
				break;
		}
	}

	/**
	 * Hàm thêm từ vào data.
	 *
	 * @param word_target  từ tiếng anh
	 * @param word_explain nghĩa của từ
	 */
	public void insertWord(String word_target, String word_explain) {
		try {
			Statement statement = con.createStatement();
			String sql = "INSERT INTO `dictionary`(`word`,`detail`) VALUES ('" + word_target + "','" + word_explain + "') ";
			statement.execute(sql);
			Service.gI().startMsgBox("Đã thêm vào từ điển!", 0, 1, null);
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
			Statement statement = con.createStatement();
			statement.executeQuery("SET SQL_SAFE_UPDATES=0 ");
			String query = "DELETE FROM `btl_dictionary`.`dictionary` WHERE `word` ='" + word_target + "'";
			statement.executeUpdate(query);
			Service.gI().startMsgBox("Đã xóa khỏi từ điển!", 0, 1, null);
		} catch (Exception e) {
			System.out.println("" + e);
		}
	}

	/**
	 * Hàm sửa word.
	 *
	 * @param word_target từ tiếng anh
	 * @param detail      nghĩa của từ
	 */
	public void fixWord(String word_target, String detail) {
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate("UPDATE `dictionary` SET `detail` ='" + detail + "' WHERE `word`='" + word_target + "'");
			Service.gI().startMsgBox("Đã sửa vào từ điển!", 0, 1, null);
		} catch (Exception e) {
			System.out.println("" + e);
		}
	}
}
