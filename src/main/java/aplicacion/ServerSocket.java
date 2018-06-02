package aplicacion;

import aplicacion.corrector.Correccion;
import aplicacion.corrector.CorrectorOrtografico;
import aplicacion.detector.DetectorRecursos;
import aplicacion.detector.Recurso;
import aplicacion.synset.ConsultorSynsets;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSocket {

    private SocketIOServer serverSocket;

    public ServerSocket(String hostname, int port) {
        Configuration configuration = new Configuration();
        configuration.setHostname(hostname);
        configuration.setPort(port);

        serverSocket = new SocketIOServer(configuration);
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

            Map<String, Map<Integer, Collection<String>>> synsets = new HashMap<>();

            recursos.forEach(recurso -> synsets.put(recurso.getName(), consultor.getSynsets(recurso.getName()).asMap()));

            socketCliente.sendEvent("listaSignificados", synsets.toString());
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
