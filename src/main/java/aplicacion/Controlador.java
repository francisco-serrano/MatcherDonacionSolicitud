package aplicacion;

import aplicacion.corrector.Correccion;
import aplicacion.corrector.CorrectorOrtografico;
import aplicacion.detector.DetectorRecursos;
import aplicacion.detector.Recurso;
import aplicacion.synset.ConsultorSynsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controlador {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();
    private ConsultorSynsets consultorSynsets;

    @Autowired
    public void setDatabase(Database database) {
        consultorSynsets = new ConsultorSynsets(database);
    }

    @RequestMapping(value = "/corrector", method = RequestMethod.GET, produces = "application/json")
    public List<Correccion> check(@RequestParam(value = "text") String plainText) {
        return corrector.check(plainText);
    }

    @RequestMapping(value = "/detector", method = RequestMethod.GET, produces = "application/json")
    public List<Recurso> detect(@RequestParam(value = "text") String plainText) {
        return detector.detect(plainText);
    }

    @RequestMapping(value = "/synsets", method = RequestMethod.GET, produces = "application/json")
    public List<Integer> getSynsets(@RequestParam(value = "text") String word) {
        return consultorSynsets.getSynsets(word);
    }
}
