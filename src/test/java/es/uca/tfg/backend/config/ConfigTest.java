package es.uca.tfg.backend.config;

import es.uca.tfg.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigTest {

    @Autowired
    PostRepository _postRepository;
}
