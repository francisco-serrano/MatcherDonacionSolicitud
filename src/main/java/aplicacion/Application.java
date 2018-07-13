package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    TODO: Modelo parcialmente documentado

    TODO: Cambiar el formato de los contadores
    TODO: Armar tabla con los contadores
    TODO: Arreglar SynsetCounter -> Al pasar como parámetro BACALAO retorna un JSON VACIO
    TODO: Ir loggeando
 */


/**
 * Clase donde se inicia la ejecución
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
@SpringBootApplication
public class Application {

    /**
     * Método principal que inicia la ejecución
     * @param args Arreglo de String con los parámetros a pasar por la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
