package aplicacion;

import aplicacion.model.Model;
import aplicacion.model.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Clase que recibe la interacción del cliente con el servidor. Se encarga de servir la página, instanciar el modelo
 * y de llamarlo en caso de solicitar los contadores.
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
@RestController
public class Controlador {

    private Model model;

    /**
     * Instancia el modelo, haciendo la correspondiente inyección de la configuración.
     *
     * @param configuration Dependencia con la configuración a inyectar en el modelo.
     */
    @Autowired
    public void createModel(ServiceConfiguration configuration) {
        model = new Model(configuration);
    }

    @RequestMapping(value = "/textanalyzer", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> analyzePlainText(
            @RequestParam(value = "text") String plainText
    ) {
        return model.generateJsonOutput(plainText);
    }

    @RequestMapping(value = "/synsetselection", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> desambiguateSynset(
            @RequestParam(value = "selection") String synsetSelection,
            @RequestParam(value = "estaDonando") boolean estaDonando
    ) {
        return model.selectSynsets(synsetSelection, estaDonando);
    }

    /**
     * Retorna un JSON con el recuento de elecciones para los synsets de cada palabra.
     * Ejemplo de salida:
     * <p>
     * {
     * "auto": {
     * "12352$$carro": 8
     * },
     * "casa": {
     * "13412$$pañuelo": 8
     * },
     * "abadejo": {
     * "78023$$bacalao": 8
     * }
     * }
     *
     * @param listaPalabras String con las palabras separadas por coma. OPCIONAL.
     * @return JSON en forma de String representando el contador para cada consultor
     */
    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<String, Integer>> getSynsetCounter(
            @RequestParam(value = "palabras", defaultValue = "") String listaPalabras
    ) {
        return model.getSynsetCounter(listaPalabras);
    }
}
