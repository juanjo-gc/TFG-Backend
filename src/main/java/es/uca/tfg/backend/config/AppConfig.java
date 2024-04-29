package es.uca.tfg.backend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class AppConfig {

    @Autowired
    private Environment env;

    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("http://" + env.getProperty("HOST_IP") + ":8080");
   }
}
