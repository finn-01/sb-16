package vn.finn.spring.configproperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ConfigPropertiesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigPropertiesApplication.class, args);


    }
    @Autowired
    AppProperties appProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Global variable:");
        System.out.println("\t Email: "+appProperties.getEmail());
        System.out.println("\t GA ID: "+appProperties.getGoogleAnalyticsId());
    }
}
