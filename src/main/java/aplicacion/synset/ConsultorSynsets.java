package aplicacion.synset;

import aplicacion.Database;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConsultorSynsets {

    private String ip;
    private String dbName;
    private String user;
    private String password;

    private Connection connection;
    private String query;

    private final Map<Integer, AtomicLong> synsetCounter = new ConcurrentHashMap<>();

    public ConsultorSynsets(Database db) {
        this.ip = db.getIp();
        this.dbName = db.getDbName();
        this.user = db.getUser();
        this.password = db.getPassword();

        connectToDatabase();
    }

    private void connectToDatabase() {

        String url = "jdbc:mysql://" + this.ip + "/" + this.dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        try {
            query = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/query_modificado.sql")).readLine();
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Multimap<Integer, String> getSynsets(String word) {
        Multimap<Integer, String> synsets = ArrayListMultimap.create();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, word.toLowerCase());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String palabra = resultSet.getString("palabra");
                int id_synset = Integer.parseInt(resultSet.getString("id_synset"));

                synsets.put(id_synset, palabra);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Integer synsetKey : synsets.keys()) {
            synsetCounter.putIfAbsent(synsetKey, new AtomicLong(0));
            synsetCounter.get(synsetKey).incrementAndGet();
        }

        return synsets;
    }

    public boolean selectSynset(int synset_id) {
        System.out.println("ABURUBA");

        synsetCounter.putIfAbsent(synset_id, new AtomicLong(0));
        synsetCounter.get(synset_id).incrementAndGet();

        return true;
    }

    public Map<Integer, AtomicLong> getSynsetCounter() {
        return synsetCounter;
    }
}
