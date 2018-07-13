package aplicacion.model;

import aplicacion.model.corrector.Correccion;
import aplicacion.model.corrector.CorrectorOrtografico;
import aplicacion.model.detector.DetectorRecursos;
import aplicacion.model.detector.Recurso;
import aplicacion.model.consultor.ConsultorSynsets;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * Constructor de la clase que se encarga de configurar la base de datos y el socket
     *
     * @param configuration Bean con la configuración de la base de datos y el socket
     */
    public Model(ServiceConfiguration configuration) {
        setDatabaseConfiguration(configuration);
    }

    public List<Correccion> detectSpellingMistakes(String text) {
        return corrector.check(text);
    }

    public List<Recurso> detectResources(String text) {
        return detector.detect(text);
    }

    public List<String> getSynsetsFromResources(List<Recurso> resourcesList) {
        return consultor.getSynsets(resourcesList);
    }

    public String selectSynsets(String synsetSelection, boolean estaDonando) {
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
}
