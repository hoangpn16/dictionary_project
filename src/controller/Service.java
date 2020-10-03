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

    public void insertWord(String word_targer, String word_explain){
        try{
            Statement statement=con.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM `dictionary` WHERE `word_targer` LIKE '%"+word_targer+"%'");
            String tmp=null;
            while(resultSet.next()){
                tmp=resultSet.getString(2);
            }
            if(tmp != null){
                System.out.println(tmp);
                System.out.println("Dữ liệu đã tồn tại");
            }else {
                String sql = "INSERT  ";
                statement.execute(sql);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public List<Word> searchWord(String wordtarger){
        List<Word> listWords=new ArrayList<>();
        try{
            Statement statement = con.createStatement();
            String query="SELECT * FROM `btl_dictionary`.`dictionary` WHERE `word_target` LIKE '%"+wordtarger+"%'";
            ResultSet resultSet=statement.executeQuery(query);
            while (resultSet.next()){
                Word word=new Word();
                word.setWord_target(resultSet.getString(2));
                word.setWord_explain(resultSet.getString(3));

                listWords.add(word);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return listWords;
    }
}
