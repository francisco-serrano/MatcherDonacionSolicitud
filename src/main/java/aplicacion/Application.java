package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    SERVICIO
    TODO: Modelo parcialmente documentado
    TODO: Ir loggeando

    BASE DE DATOS
    TODO: Actualizar en la base los contadores de synsets
    TODO: Actualizar en la base las altas de donaciones/peticiones
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
