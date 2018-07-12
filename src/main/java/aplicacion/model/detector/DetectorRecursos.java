package aplicacion.model.detector;

import operativa.system.ClasifierAND;
import operativa.system.MatchingClasifier;
import operativa.system.Resource;
import operativa.system.SintacticClasifier;

import java.util.List;
import java.util.stream.Collectors;

public class DetectorRecursos {

    public List<Recurso> detect(String plainText) {
        ClasifierAND clasifier = new ClasifierAND(new MatchingClasifier(), new SintacticClasifier());
        List<Resource> resultList = clasifier.clasify(plainText.toLowerCase());

        return resultList.stream().map(Recurso::new).collect(Collectors.toList());
    }
}
