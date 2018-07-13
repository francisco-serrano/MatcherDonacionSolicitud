package aplicacion.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Clase contenedora de la configuración a inyectar en el modelo (base de datos y servidor).
 *
 * Contiene los siguientes datos:
 *  - Dirección IP de la Base de Datos
 *  - Nombre de la Base de Datos
 *  - Username
 *  - Password
 *  - Query a correr en la base para obtener los synsets
 *  - URL del Servidor
 *  - Puerto del Socket interno del Servidor
 *
 * @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
@Component
@PropertySource("classpath:serviceconfig.properties")
@ConfigurationProperties
public class ServiceConfiguration {

    @Value("${database_ip}")
    private String database_ip;

    @Value("${database_name}")
    private String database_name;

    @Value("${database_user}")
    private String database_user;

    @Value("${database_pass}")
    private String database_pass;

    @Value("${database_query}")
    private String database_query;

    /**
     * Retorna la IP de la base de datos
     *
     * @return String con la IP de la base de datos
     */
    public String getDatabase_ip() {
        return database_ip;
    }

    /**
     * Retorna el nombre de la base de datos
     *
     * @return String con el nombre de la base de datos
     */
    public String getDatabase_name() {
        return database_name;
    }

    /**
     * Retorna el nombre de usuario para loguearse al servidor de la base
     *
     * @return String con el username del servidor de la base
     */
    public String getDatabase_user() {
        return database_user;
    }

    /**
     * Retorna la contraseña para loguearse al servidor de la base
     *
     * @return String con el password del servidor de la base
     */
    public String getDatabase_pass() {
        return database_pass;
    }

    public String getDatabase_query() {
        return database_query;
    }
}
