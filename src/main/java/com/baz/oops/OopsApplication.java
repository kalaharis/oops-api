package com.baz.oops;

import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OopsApplication implements CommandLineRunner {
    @Autowired
    private PollsRepository pollsRepository;

    public static void main(String[] args) {
        SpringApplication.run(OopsApplication.class, args);
    }

    //TODO: uncomment after dev
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("")
                        .allowedMethods("POST", "GET", "DELETE", "PUT")
                        .allowedOrigins("http://localhost:4200");
            }
        };
    }


    //TODO: remove a production
    @Override
    public void run(String... strings) throws Exception {
        /*Random rnd = new Random();
        String[] testTags = {"series", "games", "TV", "internet", "cats"};
        for (int i = 0; i < 10; i++) {

            List<Option> options = new ArrayList<>();
            for (int j = 0; j < rnd.nextInt(5); j++) {
                Option option = new Option("option " + j);
                options.add(option);
            }

            Set<String> tags = new HashSet<>();
            for (int j = 0; j < rnd.nextInt(testTags.length); j++) {
                tags.add(testTags[rnd.nextInt(testTags.length)]);
            }

            Poll poll = new Poll("poll " + i, options);
            poll.setTotalVotes(i);
            poll.setTags(tags);

            pollsRepository.save(poll);
        }*/
    }
}