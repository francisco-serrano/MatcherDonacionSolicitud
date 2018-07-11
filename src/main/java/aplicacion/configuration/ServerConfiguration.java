package aplicacion.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:server.properties")
@ConfigurationProperties
public class ServerConfiguration {

    @Value("${server_url}")
    private String url;

    @Value("${serversocket_port}")
    private String port;

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return Integer.parseInt(port);
    }
}
