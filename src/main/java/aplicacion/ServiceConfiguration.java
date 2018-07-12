package aplicacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

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

    @Value("${server_url}")
    private String server_url;

    @Value("${serversocket_port}")
    private String serversocket_port;

    public String getDatabase_ip() {
        return database_ip;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public String getDatabase_user() {
        return database_user;
    }

    public String getDatabase_pass() {
        return database_pass;
    }

    public String getServer_url() {
        return server_url;
    }

    public int getServersocket_port() {
        return Integer.parseInt(serversocket_port);
    }
}
