package controller;

import entity.Word;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Service {
    Connection con;

    public Service(Connection con) {
        this.con = con;
    }

    public Service() {
    }

    public void insertWord(String word_target, String word_explain) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word` LIKE '%" + word_target + "%'");
            String tmp = null;
            while (resultSet.next()) {
                tmp = resultSet.getString(2);
            }
            if (tmp != null) {
                System.out.println(tmp);
                System.out.println("Dữ liệu đã tồn tại");
            } else {
                String sql = "INSERT INTO `dictionary`(`word`,`detail`) VALUES ('" + word_target + "','" + word_explain + "') ";
                statement.execute(sql);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Word> searchWord(String word_target) {
        List<Word> listWords = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `diction` WHERE `word`='" + word_target + "'");
            while (resultSet.next()) {
                Word word = new Word(resultSet.getString(2), resultSet.getString(3));
                listWords.add(word);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return listWords;
    }

    public void removeWord(String word_target) {
        try {
            Statement statement = con.createStatement();
            String query = "DELETE FROM `btl_dictionary`.`dictionary` WHERE `word` LIKE '%" + word_target.toLowerCase() + "%'";
            statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Word> findWord(String keyWord) {
        List<Word> listWords = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + keyWord + "'");
            while (resultSet.next()) {
                Word word = new Word(resultSet.getString(2), resultSet.getString(3));
                if (checkItemList(listWords, word)) {
                    listWords.add(word);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM `dictionary` WHERE `word` LIKE '" + keyWord + "%' LIMIT 20";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Word rs = new Word(resultSet.getString(2), resultSet.getString(3));
                if (checkItemList(listWords, rs)) {
                    listWords.add(rs);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return listWords;
    }

    public List<Word> findByCharacter(String keyword) {
        List<Word> listWordTarget = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM `dictionary` WHERE `word` LIKE '" + keyword + "%' LIMIT 20";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Word rs = new Word(resultSet.getString(2), resultSet.getString(3));

                listWordTarget.add(rs);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return listWordTarget;
    }

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
    public void fixWord(String word_target,String detail){
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `dictionary` WHERE `word`='" + word_target + "'");
            String tmp=null;
            while (resultSet.next()) {
              tmp=resultSet.getString(2);
            }
            if (tmp == null){
                System.out.println("Khong ton tai tu");
            }else {
                statement.executeQuery("UPDATE `dictionary` SET `detail` ="+detail+" WHERE `word`="+word_target+"");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
