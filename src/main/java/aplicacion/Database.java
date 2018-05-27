package aplicacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:database.properties")
@ConfigurationProperties
public class Database {

    @Value("${ip}")
    private String ip;

    @Value("${db_name}")
    private String dbName;

    @Value("${user}")
    private String user;

    @Value("${password}")
    private String password;

    public String getIp() {
        return ip;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
