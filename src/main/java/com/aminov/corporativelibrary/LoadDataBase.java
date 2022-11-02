package com.aminov.corporativelibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aminov.corporativelibrary.models.Author;
import com.aminov.corporativelibrary.repositories.AuthorRepository;

@Configuration
public class LoadDataBase {

    private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

    @Bean
    CommandLineRunner initDataBase(AuthorRepository repository){
        return args -> {
            log.info("Preloading " + repository.save(new Author("Aminov", "Arslan", "Gaynetdinovich")));
            log.info("Preloading " + repository.save(new Author("Ivanov", "Ivan", "Ivanovich")));
            log.info("Preloading " + repository.save(new Author("Kek", "Lol", "Ahaha")));
            log.info("Preloading " + repository.save(new Author("Jackson", "Michael", "Mich")));
        };
    }
    
}
