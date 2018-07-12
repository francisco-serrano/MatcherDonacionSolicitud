package aplicacion;

import aplicacion.model.Model;
import aplicacion.model.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Instancia el modelo, haciendo la correspondiente inyección de la dependencia con su configuración.
     * @param configuration Dependencia con la configuración a inyectar en el modelo.
     */
    @Autowired
    public void createModel(ServiceConfiguration configuration) {
        model = new Model(configuration);
    }

    /**
     * Retorna un String con la página web principal.
     * @return String con la página web principal
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() {
        return serveHtml();
    }

    /**
     * Retorna un JSON con el recuento de elecciones para los synsets de cada palabra.
     * Ejemplo de salida:
     *
     * {
     *     "auto": {
     *         "12352$$carro": 8
     *     },
     *     "casa": {
     *         "13412$$pañuelo": 8
     *     },
     *     "abadejo": {
     *         "78023$$bacalao": 8
     *     }
     * }
     *
     * @param listaPalabras String con las palabras separadas por coma. OPCIONAL.
     * @return JSON en forma de String representando el contador para cada consultor
     */
    @RequestMapping(value = "/synsetcounter", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Map<String, Integer>> getSynsetCounter(@RequestParam(value = "palabras", defaultValue = "") String listaPalabras) {
        // La lista de palabras debe estar separada por coma
        return model.getSynsetCounter(listaPalabras);
    }

    /**
     * Retorna el HTML en forma de String.
     * @return String con la página
     */
    private String serveHtml() {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\index.html")), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
