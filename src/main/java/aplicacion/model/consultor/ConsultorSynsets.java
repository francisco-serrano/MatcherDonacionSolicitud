package aplicacion.model.consultor;

import aplicacion.model.ServiceConfiguration;
import aplicacion.model.detector.Recurso;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ConsultorSynsets {

    private Connection connection;
    private String query;

    /*
        seleccionSynsetsUsuario
        Almacena cada desambiguación que el user envía
        Ahora se almacena en memoria pero debería ir a la base
     */
    private final List<SeleccionSynset> seleccionSynsetsUsuario = new ArrayList<>();

    public void setUpDatabase(ServiceConfiguration configuration) {
        String url = "jdbc:mysql://" + configuration.getDatabase_ip() + "/" + configuration.getDatabase_name() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        try {
            query = configuration.getDatabase_query();
            connection = DriverManager.getConnection(url, configuration.getDatabase_user(), configuration.getDatabase_pass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSynsets(List<Recurso> resourcesList) {
        Map<String, Map<Integer, Collection<String>>> synsetMapping = resourcesList.stream()
                .collect(Collectors.toMap(
                        Recurso::getName,
                        recurso -> getSynsets(recurso.getName()).asMap()
                ));

        List<String> synsetMappingFormatted = new ArrayList<>();
        String pattern = "[\\[\\]]";

        synsetMapping.forEach((palabra, synsets) -> synsets.forEach(
                (id_synset, lista_sinonimos) -> synsetMappingFormatted.add(
                        (palabra + ":" + id_synset + ":" + lista_sinonimos.toString()).replaceAll(pattern, "")
                )
        ));

        return synsetMappingFormatted;
    }

    private Multimap<Integer, String> getSynsets(String word) {
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

    public Map<String, Object> selectSynsets(String synsetSelection, boolean estaDonando) {
        Map<String, Object> jsonRetornar = new HashMap<>();

        if (synsetSelection.length() == 0) {
            jsonRetornar.put("mensaje", "Se mandó un String vacío");

            return jsonRetornar;
        }

        // Se agrega a una lista interna
        List<String> listaDesambiguaciones = Splitter.on("; ").splitToList(synsetSelection);
        listaDesambiguaciones.forEach(str -> {
            List<String> elementosSeleccion = Splitter.on(":").splitToList(str);

            String recurso = elementosSeleccion.get(0);
            int idSynset = Integer.parseInt(elementosSeleccion.get(1));
            String sinonimos = elementosSeleccion.get(2);

            // TODO: Dar de alta la selección de synset(desambiguación) en la base de datos
            seleccionSynsetsUsuario.add(new SeleccionSynset(recurso, idSynset, sinonimos, estaDonando));
        });

        jsonRetornar.put("mensaje", "Desambiguación almacenada correctamente");
        jsonRetornar.put("desambiguaciones-almacenadas", listaDesambiguaciones);

        return jsonRetornar;
    }

    public Map<String, Map<String, Integer>> getSynsetCounter(String listaPalabras) {
        // Almacena los synsets relacionados a un solo recurso. Ej: (manta) -> {12345, 34567, 56789}
        Multimap<String, Integer> mapeoPalabraSynsets = ArrayListMultimap.create();

        // Mapea cada idSynset con las demás palabras que contiene el synset
        Map<Integer, String> mapeoSynsetPalabra = new HashMap<>();

        // TODO: Implementar que seleccionSynsetsUsuario venga de la base de datos
        seleccionSynsetsUsuario.forEach(seleccion -> {
            mapeoPalabraSynsets.put(seleccion.getRecurso(), seleccion.getIdSynset());
            mapeoSynsetPalabra.put(seleccion.getIdSynset(), seleccion.getSinonimos());
        });

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

            synsetCounter.forEach((synset_id, cantidad) -> synsetSynonymCounter.put(
                    synset_id + "$$" + mapeoSynsetPalabra.get(synset_id), Math.toIntExact(cantidad.get()))
            );

            wordSynsetSynonymCounter.put(palabra, synsetSynonymCounter);
        });

        List<String> palabrasObtener = Splitter.on(',').splitToList(listaPalabras);

        if (palabrasObtener.size() == 1 && (palabrasObtener.get(0).equals("") || !mapeoPalabraSynsets.keySet().contains(palabrasObtener.get(0))))
            return wordSynsetSynonymCounter;

        wordSynsetSynonymCounter.keySet().stream()
                .filter(palabra -> !palabrasObtener.contains(palabra))
                .collect(Collectors.toList())
                .forEach(wordSynsetSynonymCounter::remove);

        return wordSynsetSynonymCounter;
    }
}
