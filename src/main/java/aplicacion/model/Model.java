package aplicacion.model;

import aplicacion.model.corrector.CorrectorOrtografico;
import aplicacion.model.detector.DetectorRecursos;
import aplicacion.model.socket.ServerSocket;
import aplicacion.model.consultor.ConsultorSynsets;

import java.util.Map;

/**
 * Clase que engloba a las distintas herramientas del servicio, a saber:
 *  - Corrector Ortográfico
 *  - Detector de Recursos
 *  - Consultor de Synsets
 *  - Socket del Servicio
 *
 *  @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
public class Model {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultorSynsets = new ConsultorSynsets();
    private ServerSocket serverSocket = new ServerSocket();

    /**
     * Constructor de la clase que se encarga de configurar la base de datos y el socket
     * @param configuration Bean con la configuración de la base de datos y el socket
     */
    public Model(ServiceConfiguration configuration) {
        setDatabaseConfiguration(configuration);
        setServerConfiguration(configuration);
    }

    /**
     * Retorna un mapeo con los contadores de cada consultor para cada palabra, en caso de pasar por parámetro un
     * String vacío, se retornan los contadores para todas las palabras.
     *
     * @param listaPalabras String con las palabras a filtrar en caso de solicitarlo
     * @return Mapeo con los contadores de synsets para las palabras solicitadas
     */
    public Map<String, Map<String, Integer>> getSynsetCounter(String listaPalabras) {
        return consultorSynsets.getSynsetCounter(listaPalabras);
    }

    /**
     * Reenvía la configuración al Consultor de Synsets para que levante la base
     * @param configuration Bean con la configuración de la base de datos
     */
    private void setDatabaseConfiguration(ServiceConfiguration configuration) {
        consultorSynsets.setUpDatabase(configuration);
    }

    /**
     * Reenvía la configuración al ServerSocket para poder crearlo
     * @param configuration Bean con la configuración del socket
     */
    private void setServerConfiguration(ServiceConfiguration configuration) {
        serverSocket.setupServer(configuration);
        serverSocket.setUpListeners(corrector, detector, consultorSynsets);
        serverSocket.start();
    }
}
