package aplicacion.model.detector;

import operativa.system.ClasifierAND;
import operativa.system.MatchingClasifier;
import operativa.system.Resource;
import operativa.system.SintacticClasifier;

import java.util.List;
import java.util.stream.Collectors;

public class DetectorRecursos {
    public List<Resource> detect(String plainText) {
        ClasifierAND clasifier = new ClasifierAND(new MatchingClasifier(), new SintacticClasifier());

        return clasifier.clasify(plainText.toLowerCase());
    }
}
