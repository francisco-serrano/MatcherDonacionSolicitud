package aplicacion;

import aplicacion.configuration.DatabaseConfiguration;
import aplicacion.configuration.ServerConfiguration;
import aplicacion.corrector.CorrectorOrtografico;
import aplicacion.detector.DetectorRecursos;
import aplicacion.synset.ConsultorSynsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class Controlador {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultorSynsets = new ConsultorSynsets();

    private ServerSocket serverSocket = new ServerSocket();

    @Autowired
    public void setDatabaseConfiguration(DatabaseConfiguration databaseConfiguration) {
        consultorSynsets.setUpDatabase(databaseConfiguration);
    }

    @Autowired
    public void setServerConfiguration(ServerConfiguration serverConfiguration) {
        serverSocket.setupServer(serverConfiguration);
        serverSocket.setUpListeners(corrector, detector, consultorSynsets);
        serverSocket.start();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() throws IOException {
        return serveHtml();
    }

    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<String, Integer>> getSynsetCounter(@RequestParam(value = "palabras", defaultValue = "") String listaPalabras) {
        // La lista de palabras debe estar separada por coma
        return consultorSynsets.getSynsetCounter(listaPalabras);
    }

    private String serveHtml() {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\index.html")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
