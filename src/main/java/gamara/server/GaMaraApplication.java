package gamara.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GaMaraApplication {

    public static void main(String[] args) {
        SpringApplication.run(GaMaraApplication.class, args);
    }
}
