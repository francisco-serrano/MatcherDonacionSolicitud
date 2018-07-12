package aplicacion.synset;

import aplicacion.ServiceConfiguration;
import aplicacion.configuration.DatabaseConfiguration;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ConsultorSynsets {

    private Connection connection;
    private String query;

    private final List<String> seleccionSynsetsUsuario = new CopyOnWriteArrayList<>();

//    public void setUpDatabase(DatabaseConfiguration db) {
//        String url = "jdbc:mysql://" + db.getIp() + "/" + db.getDbName() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//
//        try {
//            query = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/query_modificado.sql")).readLine();
//            connection = DriverManager.getConnection(url, db.getUser(), db.getPassword());
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void setUpDatabase(ServiceConfiguration configuration) {
        String url = "jdbc:mysql://" + configuration.getDatabase_ip() + "/" + configuration.getDatabase_name() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        try {
            query = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/query_modificado.sql")).readLine();
            connection = DriverManager.getConnection(url, configuration.getDatabase_user(), configuration.getDatabase_pass());
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

        return synsets;
    }

    public String selectSynsets(String synsetSelection) {
        if (synsetSelection.length() == 0) {
            System.out.println("SE MANDO UN STRING VACIO --> NINGUNO DE LOS SYNSETS ENVIADOS SE CORRESPONDEN!!!");

            // TODO: Ir a la base y agregarlo
        }

        seleccionSynsetsUsuario.addAll(Splitter.on("; ").splitToList(synsetSelection));

        return "Selección almacenada";
    }

    public Map<String, Map<String, Integer>> getSynsetCounter(String listaPalabras) {
        Multimap<String, Integer> mapeoPalabraSynsets = ArrayListMultimap.create();

        Map<Integer, String> mapeoSynsetPalabra = new HashMap<>();

        seleccionSynsetsUsuario.forEach(s -> {
            List<String> aux2 = Splitter.on(":").splitToList(s);

            assert aux2.size() == 3;

            mapeoPalabraSynsets.put(aux2.get(0), Integer.parseInt(aux2.get(1)));

            mapeoSynsetPalabra.put(Integer.parseInt(aux2.get(1)), aux2.get(2));
        });

        // {'manta': {23345: 1, 45553: 3}, 'casa': {34431: 5, 3234: 2}}
        Map<String, Map<Integer, AtomicLong>> wordSynsetCounter = new HashMap<>();

        mapeoPalabraSynsets.keySet().forEach(word -> {
            Map<Integer, AtomicLong> contadorSynsets = new HashMap<>();

            mapeoPalabraSynsets.get(word).forEach(synsetId -> {
                contadorSynsets.putIfAbsent(synsetId, new AtomicLong(0));
                contadorSynsets.get(synsetId).incrementAndGet();
            });

            wordSynsetCounter.put(word, contadorSynsets);
        });

        Map<String, Map<String, Integer>> wordSynsetSynonymCounter = new HashMap<>();

        wordSynsetCounter.forEach((palabra, synsetCounter) -> {
            Map<String, Integer> synsetSynonymCounter = new HashMap<>();

            synsetCounter.forEach((synset_id, cantidad) -> {
                synsetSynonymCounter.put(synset_id + "$$" + mapeoSynsetPalabra.get(synset_id), Math.toIntExact(cantidad.get()));
            });

            wordSynsetSynonymCounter.put(palabra, synsetSynonymCounter);
        });

        List<String> palabrasObtener = Splitter.on(',').splitToList(listaPalabras);

        if (palabrasObtener.size() == 1 && palabrasObtener.get(0).equals(""))
            return wordSynsetSynonymCounter;

        wordSynsetSynonymCounter.keySet().stream()
                .filter(palabra -> !palabrasObtener.contains(palabra))
                .collect(Collectors.toList())
                .forEach(wordSynsetSynonymCounter::remove);

        return wordSynsetSynonymCounter;
    }
}
