package aplicacion;

import aplicacion.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class Controlador {

//    private CorrectorOrtografico corrector = new CorrectorOrtografico();
//    private DetectorRecursos detector = new DetectorRecursos();
//    private ConsultorSynsets consultorSynsets = new ConsultorSynsets();
//    private ServerSocket serverSocket = new ServerSocket();

    private Model model;

    @Autowired
    public void createModel(ServiceConfiguration configuration) {
        model = new Model(configuration);
    }

//    private void setDatabaseConfiguration(ServiceConfiguration configuration) {
//        consultorSynsets.setUpDatabase(configuration);
//    }
//
//    private void setServerConfiguration(ServiceConfiguration configuration) {
//        serverSocket.setupServer(configuration);
//        serverSocket.setUpListeners(corrector, detector, consultorSynsets);
//        serverSocket.start();
//    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() {
        return serveHtml();
    }

    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<String, Integer>> getSynsetCounter(@RequestParam(value = "palabras", defaultValue = "") String listaPalabras) {
        // La lista de palabras debe estar separada por coma
//        return consultorSynsets.getSynsetCounter(listaPalabras);
        return model.getSynsetCounter(listaPalabras);
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
