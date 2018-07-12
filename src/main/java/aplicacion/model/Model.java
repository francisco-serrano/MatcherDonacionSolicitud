package aplicacion.model;

import aplicacion.ServiceConfiguration;
import aplicacion.model.corrector.CorrectorOrtografico;
import aplicacion.model.detector.DetectorRecursos;
import aplicacion.model.socket.ServerSocket;
import aplicacion.model.synset.ConsultorSynsets;

import java.util.Map;

public class Model {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultorSynsets = new ConsultorSynsets();
    private ServerSocket serverSocket = new ServerSocket();

    public Model(ServiceConfiguration configuration) {
        setDatabaseConfiguration(configuration);
        setServerConfiguration(configuration);
    }

    public Map<String, Map<String, Integer>> getSynsetCounter(String listaPalabras) {
        return consultorSynsets.getSynsetCounter(listaPalabras);
    }

    private void setDatabaseConfiguration(ServiceConfiguration configuration) {
        consultorSynsets.setUpDatabase(configuration);
    }

    private void setServerConfiguration(ServiceConfiguration configuration) {
        serverSocket.setupServer(configuration);
        serverSocket.setUpListeners(corrector, detector, consultorSynsets);
        serverSocket.start();
    }
}
