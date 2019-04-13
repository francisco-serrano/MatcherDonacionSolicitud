package aplicacion.model;

import aplicacion.model.corrector.Correccion;
import aplicacion.model.corrector.CorrectorOrtografico;
import aplicacion.model.detector.DetectorRecursos;
import aplicacion.model.detector.Recurso;
import aplicacion.model.consultor.ConsultorSynsets;
import aplicacion.model.detector.RecursoDetectado;
import com.google.common.collect.Multimap;
//import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que engloba a las distintas herramientas del servicio, a saber:
 * - Corrector Ortográfico
 * - Detector de Recursos
 * - Consultor de Synsets
 * - Socket del Servicio
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
public class Model {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultor = new ConsultorSynsets();

    private static String resultado_corrector;
    private static String resultado_detector;

    /**
     * Constructor de la clase que se encarga de configurar la base de datos y el socket
     *
     * @param configuration Bean con la configuración de la base de datos y el socket
     */
    public Model(ServiceConfiguration configuration) {
        setDatabaseConfiguration(configuration);

        resultado_corrector = configuration.getResultado_corrector();
        resultado_detector = configuration.getResultado_detector();
    }

    public Map<String, Object> generateJsonOutput(String plainText) {
        List<Correccion> correcciones = detectSpellingMistakes(plainText);
        if (!correcciones.isEmpty())
            return Model.generateErrorMessage(resultado_corrector, correcciones);

        List<Recurso> recursosDetectados = detectResources(plainText);
        if (recursosDetectados.isEmpty())
            return Model.generateErrorMessage(resultado_detector);

        List<String> valoresRadioButton = getSynsetsFromResources(recursosDetectados);

        var parRecursosDesambiguaciones = formatDetectedResourcesList(valoresRadioButton);

        Map<String, Object> jsonGigante = new HashMap<>();
        jsonGigante.put("valores-radio-button", valoresRadioButton);
        jsonGigante.put("recursos-detectados", parRecursosDesambiguaciones.getKey());
        jsonGigante.put("lista-desambiguaciones", parRecursosDesambiguaciones.getValue().asMap());

        return jsonGigante;
    }

    public Map<String, Object> selectSynsets(String synsetSelection, boolean estaDonando) {
        return consultor.selectSynsets(synsetSelection, estaDonando);
    }

    /**
     * Retorna un mapeo con los contadores de cada consultor para cada palabra, en caso de pasar por parámetro un
     * String vacío, se retornan los contadores para todas las palabras.
     *
     * @param listaPalabras String con las palabras a filtrar en caso de solicitarlo
     * @return Mapeo con los contadores de synsets para las palabras solicitadas
     */
    public Map<String, Map<String, Integer>> getSynsetCounter(String listaPalabras) {
        return consultor.getSynsetCounter(listaPalabras);
    }

    /**
     * Reenvía la configuración al Consultor de Synsets para que levante la base
     *
     * @param configuration Bean con la configuración de la base de datos
     */
    private void setDatabaseConfiguration(ServiceConfiguration configuration) {
        consultor.setUpDatabase(configuration);
    }

    private List<Correccion> detectSpellingMistakes(String text) {
        return corrector.check(text);
    }

    private List<Recurso> detectResources(String text) {
        return detector.detect(text);
    }

    private List<String> getSynsetsFromResources(List<Recurso> resourcesList) {
        return consultor.getSynsets(resourcesList);
    }

    private Map.Entry<List<RecursoDetectado>, Multimap<String, Object>> formatDetectedResourcesList(List<String> resourcesList) {
        return ModelUtils.formatDetectedResourcesList(resourcesList);
    }

    private static Map<String, Object> generateErrorMessage(String message, Object optionalData) {
        Map<String, Object> jsonRetornar = generateErrorMessage(message);
        jsonRetornar.put("Información Adicional", optionalData);

        return jsonRetornar;
    }

    private static Map<String, Object> generateErrorMessage(String message) {
        Map<String, Object> jsonRetornar = new HashMap<>();
        jsonRetornar.put("Mensaje", message);

        return jsonRetornar;
    }

}
