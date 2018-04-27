package corrector;

import detector.DetectorRecursos;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CorrectorController {

    private CorrectorOrtografico corrector = new CorrectorOrtografico();
    private DetectorRecursos detector = new DetectorRecursos();

    @RequestMapping(value = "/corrector", method = RequestMethod.GET, produces = "application/json")
    public List<Correccion> check(@RequestParam(value = "text") String plainText) {
        return corrector.check(plainText);
    }

    @RequestMapping(value = "/detector", method = RequestMethod.GET, produces = "application/json")
    public String detect(@RequestParam(value = "text") String plainText) {
        return detector.detect(plainText);
    }
}
