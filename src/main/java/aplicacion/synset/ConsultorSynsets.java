package aplicacion.synset;

import aplicacion.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultorSynsets {

    private String ip;
    private String dbName;
    private String user;
    private String password;

    public ConsultorSynsets(Database db) {
        this.ip = db.getIp();
        this.dbName = db.getDbName();
        this.user = db.getUser();
        this.password = db.getPassword();

        readDatabase();
    }

    private void readDatabase() {

        String url = "jdbc:mysql://" + this.ip + "/" + this.dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from variant limit 10;");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("word") + ", " +
                                resultSet.getString("sense") + ", " +
                                resultSet.getString("synset")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getSynsets(String word) {
        List<Integer> listaRetornar = new ArrayList<>();

        // ACA HACER EL QUERY

        return listaRetornar;
    }
}
