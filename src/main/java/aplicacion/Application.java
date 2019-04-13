package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/*
    TODO: Ir loggeando lo que pasa
    TODO: Acomodar el código que es un asco
    TODO: Dejar el ID de recurso listo para levantarlo a partir del query
    TODO: Tratar de no usar Object en el retorno del JSON
    TODO: Tratar de documentar el código
 */


/**
 * Clase donde se inicia la ejecución
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
@SpringBootApplication
@EnableWebMvc
@Configuration
public class Application {

    /**
     * Método principal que inicia la ejecución
     * @param args Arreglo de String con los parámetros a pasar por la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
