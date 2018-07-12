package aplicacion.model.socket;

import aplicacion.ServiceConfiguration;
import aplicacion.model.corrector.Correccion;
import aplicacion.model.corrector.CorrectorOrtografico;
import aplicacion.model.detector.DetectorRecursos;
import aplicacion.model.detector.Recurso;
import aplicacion.model.synset.ConsultorSynsets;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.base.Joiner;

import java.util.*;

public class ServerSocket {

    private SocketIOServer serverSocket;

    public void setupServer(ServiceConfiguration configuration) {
        Configuration conf = new Configuration();
        conf.setHostname(configuration.getServer_url());
        conf.setPort(configuration.getServersocket_port());

        serverSocket = new SocketIOServer(conf);
    }

    public void setUpListeners(CorrectorOrtografico corrector, DetectorRecursos detector, ConsultorSynsets consultor) {
        serverSocket.addConnectListener(socketIOClient -> System.out.println("Se conectó un cliente"));

        serverSocket.addEventListener("textoTraducir", String.class, (socketCliente, mensajeRecibido, ackRequest) -> {
            System.out.println("(textoTraducir) -> " + mensajeRecibido);

            List<Correccion> correciones = corrector.check(mensajeRecibido);

            socketCliente.sendEvent("resultadoCorreccion", correciones.toString());

            if (correciones.size() != 0)
                return;

            List<Recurso> recursos = detector.detect(mensajeRecibido);

            socketCliente.sendEvent("resultadoDeteccion", recursos.toString());

            Map<String, Map<Integer, Collection<String>>> synset_mapping = new HashMap<>();

            recursos.forEach(recurso ->
                    synset_mapping.put(recurso.getName(), consultor.getSynsets(recurso.getName()).asMap())
            );

            List<String> valoresRadioButton = new ArrayList<>();
            String pattern = "[\\[\\]]";

            synset_mapping.forEach((palabra, synsets) -> {
                synsets.forEach((id_synset, lista_sinonimos) -> {
                    valoresRadioButton.add((palabra + ":" + id_synset + ":" + lista_sinonimos.toString())
                            .replaceAll(pattern, "")
                    );
                });
            });

            socketCliente.sendEvent("listaSignificados", Joiner.on(';').join(valoresRadioButton).toString());
        });

        serverSocket.addEventListener("synsetElegido", Map.class, (socketCliente, map, ackRequest) -> {
            // las claves son "content" : STRING, "estaDonando" : BOOLEAN
            System.out.println("(synsetElegido) -> " + map);

            // TODO: llamar a un MATCHER

            socketCliente.sendEvent("resultadoActualizarContador", consultor.selectSynsets((String) map.get("content")));
        });
    }

    public void start() {
        serverSocket.start();
    }
}
