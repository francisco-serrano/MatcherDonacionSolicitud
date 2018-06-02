package aplicacion;

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
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controlador {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultorSynsets = new ConsultorSynsets();

    @Autowired
    public Controlador() {
        ServerSocket serverSocket = new ServerSocket("localhost", 8081); //TODO: parametrizarlo
        serverSocket.setUpListeners(corrector, detector, consultorSynsets);
        serverSocket.start();
    }

    @Autowired
    public void setDatabase(Database database) {
        consultorSynsets.setUpDatabase(database);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() throws IOException {
        return serveHtml();
    }

    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<Integer, AtomicLong>> getSynsetCounter() {
        return consultorSynsets.getSynsetCounter();
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
