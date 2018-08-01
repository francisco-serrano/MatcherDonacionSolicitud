package aplicacion;

import aplicacion.model.Model;
import aplicacion.model.RecursoDetectado;
import aplicacion.model.ServiceConfiguration;
import aplicacion.model.corrector.Correccion;
import aplicacion.model.detector.Recurso;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que recibe la interacción del cliente con el servidor. Se encarga de servir la página, instanciar el modelo
 * y de llamarlo en caso de solicitar los contadores.
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
@RestController
public class Controlador {

    private Model model;

    private static final String RESULTADO_CORRECTOR = "RESULTADO: ERRORES ORTOGRAFICOS DETECTADOS\n";
    private static final String RESULTADO_DETECTOR = "RESULTADO: NO SE DETECTARON RECURSOS\n";
    private static final String RESULTADO_SYNSETS = "RESULTADO: SYNSETS ENCONTRADOS\n";

    /**
     * Instancia el modelo, haciendo la correspondiente inyección de la configuración.
     *
     * @param configuration Dependencia con la configuración a inyectar en el modelo.
     */
    @Autowired
    public void createModel(ServiceConfiguration configuration) {
        model = new Model(configuration);
    }

    @RequestMapping(value = "/textanalyzer", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> analyzePlainText(
            @RequestParam(value = "text") String plainText
    ) {
        List<Correccion> correcciones = model.detectSpellingMistakes(plainText);

        if (!correcciones.isEmpty()) {
            Map<String, Object> jsonRetornar = new HashMap<>();
            jsonRetornar.put("Mensaje", RESULTADO_CORRECTOR);
            jsonRetornar.put("Correcciones", correcciones);

            return jsonRetornar;
        }

        List<Recurso> recursosDetectados = model.detectResources(plainText);

        if (recursosDetectados.isEmpty()) {
            Map<String, Object> jsonRetornar = new HashMap<>();
            jsonRetornar.put("Mensaje", RESULTADO_DETECTOR);

            return jsonRetornar;
        }

        List<String> valoresRadioButton = model.getSynsetsFromResources(recursosDetectados);

        Multimap<String, Object> listaDesambiguaciones = ArrayListMultimap.create();
        List<Map<String, Object>> listaRecursosDetectados = valoresRadioButton.stream()
                .map(valorRadioButton -> Splitter.on(':').splitToList(valorRadioButton))
                .map(listaValores -> {
                            RecursoDetectado recursoDetectado = new RecursoDetectado(1234, Integer.parseInt(listaValores.get(1)), listaValores.get(0), 1);
                            listaDesambiguaciones.put(listaValores.get(0), recursoDetectado);
                            return recursoDetectado;
                        }
                )
                .map(RecursoDetectado::toMap)
                .collect(Collectors.toList());


        Map<String, Object> jsonGigante = new HashMap<>();
        jsonGigante.put("valores-radio-button", valoresRadioButton);
        jsonGigante.put("recursos-detectados", listaRecursosDetectados);
        jsonGigante.put("lista-desambiguaciones", listaDesambiguaciones.asMap());

        return jsonGigante;
    }

    @RequestMapping(value = "/synsetselection", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> desambiguateSynset(
            @RequestParam(value = "selection") String synsetSelection,
            @RequestParam(value = "estaDonando") boolean estaDonando
    ) {
        return model.selectSynsets(synsetSelection, estaDonando);
    }

    /**
     * Retorna un JSON con el recuento de elecciones para los synsets de cada palabra.
     * Ejemplo de salida:
     * <p>
     * {
     * "auto": {
     * "12352$$carro": 8
     * },
     * "casa": {
     * "13412$$pañuelo": 8
     * },
     * "abadejo": {
     * "78023$$bacalao": 8
     * }
     * }
     *
     * @param listaPalabras String con las palabras separadas por coma. OPCIONAL.
     * @return JSON en forma de String representando el contador para cada consultor
     */
    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<String, Integer>> getSynsetCounter(
            @RequestParam(value = "palabras", defaultValue = "") String listaPalabras
    ) {
        return model.getSynsetCounter(listaPalabras);
    }
}
