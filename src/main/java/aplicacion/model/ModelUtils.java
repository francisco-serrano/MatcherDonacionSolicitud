package aplicacion.model;

import aplicacion.model.detector.RecursoDetectado;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ModelUtils {

    public static Pair<List<RecursoDetectado>, Multimap<String, Object>> formatDetectedResourcesList(List<String> resourcesList) {
        Multimap<String, Object> listaDesambiguaciones = ArrayListMultimap.create();
        List<RecursoDetectado> listaRecursosDetectados = resourcesList.stream()
                .map(resource -> Splitter.on(':').splitToList(resource))
                .map(listaValores -> {
                            RecursoDetectado recursoDetectado = new RecursoDetectado(1234, Integer.parseInt(listaValores.get(1)), listaValores.get(0), 1);
                            listaDesambiguaciones.put(listaValores.get(0), recursoDetectado);
                            return recursoDetectado;
                        }
                )
                .collect(Collectors.toList());

        return new Pair<>(listaRecursosDetectados, listaDesambiguaciones);
    }

}
