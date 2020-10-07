import controller.Service;
import entity.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println("-------- MySQL JDBC Connection Demo ------------");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/btl_dictionary?characterEncoding=UTF-8&autoReconnect=true&connectTimeout=30000&socketTimeout=30000&serverTimezone=UTC", "root", "17072000");
            System.out.println("SQL Connection to database established!");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println(e);
            return;
        }
        Service service=new Service(connection);
        List<Word> listresult=service.findByCharacter("d");
        for (Word rs:listresult) {
            System.out.println(rs.getInfor());
        }
    }
}
