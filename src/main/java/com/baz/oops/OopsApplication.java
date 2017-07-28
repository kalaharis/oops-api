package com.baz.oops;

import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.crypt.Hashids;
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OopsApplication implements CommandLineRunner {
    //TODO: remove at production
    private final String SALT = System.getenv("SALT");
    private final String MIN_HASH_LENGTH = System.getenv("MIN_HASH_LENGTH");
    @Autowired
    private PollsRepository pollsRepository;
    //

    public static void main(String[] args) {
        SpringApplication.run(OopsApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String origins = System.getenv("ORIGINS");
                log.info("Configuring cors for origins: " + origins);
                registry.addMapping("/**")
                        .allowedMethods("POST", "GET", "DELETE", "PUT")
                        .allowedOrigins(origins);
            }
        };
    }

    private String createPublicId(long privateId) {
        log.info("encryption of private id...");
        log.info("result: private id = " + privateId);
        Hashids hashids = new Hashids(SALT, Integer.parseInt(MIN_HASH_LENGTH));
        String publicId = hashids.encrypt(privateId);

        log.info("given: public id = " + publicId);
        return publicId;
    }

    //TODO: remove at production
    @Override
    public void run(String... strings) throws Exception {
        if (pollsRepository.count() == 0) {
            fillDbWithTestPolls();
        }
    }

    private void fillDbWithTestPolls() {
        //test poll 1
        Poll poll = new Poll("Favorite game genre");

        Option opt = new Option("RPG");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("Sports");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("RTS");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("Platformer");
        opt.setVotesCount(56);
        poll.addOption(opt);

        opt = new Option("ARPG");
        opt.setVotesCount(34);
        poll.addOption(opt);

        opt = new Option("FPS");
        opt.setVotesCount(29);
        poll.addOption(opt);

        opt = new Option("MMO");
        opt.setVotesCount(28);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85 + 64 + 56 + 34 + 29 + 28);
        poll.setMultiOptions(false);

        Set<String> tags = new HashSet<>();
        tags.add("games");
        tags.add("PC");
        tags.add("playstation");
        tags.add("XBOX");
        poll.setTags(tags);

        poll.setExpireDate(new Date());
        poll.setPublicId(createPublicId(1));

        pollsRepository.save(poll);
        //1

        //test poll 2
        poll = new Poll("Do you like secrets?");

        opt = new Option("Yes");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("No");
        opt.setVotesCount(85);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85);
        poll.setMultiOptions(false);

        tags = new HashSet<>();
        tags.add("secret");
        poll.setTags(tags);
        poll.setPublicId(createPublicId(2));

        pollsRepository.save(poll);

        //test poll 3
        poll = new Poll("Transport of choice?");

        opt = new Option("car");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("train");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("bike");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("motobike");
        opt.setVotesCount(56);
        poll.addOption(opt);

        opt = new Option("plain");
        opt.setVotesCount(34);
        poll.addOption(opt);

        opt = new Option("boat");
        opt.setVotesCount(29);
        poll.addOption(opt);

        opt = new Option("space rocket");
        opt.setVotesCount(28);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85 + 64 + 56 + 34 + 29 + 28);
        poll.setMultiOptions(false);

        tags = new HashSet<>();
        tags.add("funny");
        tags.add("social");
        tags.add("multiple choices");
        poll.setTags(tags);

        poll.setExpireDate(new Date(3147109200000L));
        poll.setPublicId(createPublicId(3));

        pollsRepository.save(poll);

        //test poll 4
        poll = new Poll("Best ongoing tv series");

        opt = new Option("Game of thrones");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("Lucifer");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("Preacher");
        opt.setVotesCount(64);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85 + 64);
        poll.setMultiOptions(false);

        poll.setPublicId(createPublicId(4));

        pollsRepository.save(poll);

        //test poll 5
        poll = new Poll("What programming languages do you use?");

        opt = new Option("Java");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("C#");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("Javascript");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("C/C++");
        opt.setVotesCount(56);
        poll.addOption(opt);

        opt = new Option("Swift");
        opt.setVotesCount(34);
        poll.addOption(opt);

        opt = new Option("Ruby");
        opt.setVotesCount(29);
        poll.addOption(opt);

        opt = new Option("Objective-C");
        opt.setVotesCount(28);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85 + 64 + 56 + 34 + 29 + 28);
        poll.setMultiOptions(true);

        tags = new HashSet<>();
        tags.add("programming");
        tags.add("PC");
        tags.add("multiple choices");
        poll.setTags(tags);
        poll.setPublicId(createPublicId(5));

        pollsRepository.save(poll);
        //5

        //test poll 6
        poll = new Poll("Favourite colour?");

        opt = new Option("Black");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("White");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("Green");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("Red");
        opt.setVotesCount(56);
        poll.addOption(opt);

        opt = new Option("Yellow");
        opt.setVotesCount(34);
        poll.addOption(opt);

        opt = new Option("Purple");
        opt.setVotesCount(29);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85 + 64 + 56 + 34 + 29);
        poll.setMultiOptions(false);
        poll.setPublicId(createPublicId(6));


        pollsRepository.save(poll);
        //6

        //test poll 7
        poll = new Poll("Console of choice");

        opt = new Option("Screw console, i choose PC");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("Playstation");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("XBOX");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("Wii");
        opt.setVotesCount(56);
        poll.addOption(opt);

        tags = new HashSet<>();
        tags.add("games");
        tags.add("PC");
        tags.add("playstation");
        tags.add("XBOX");
        tags.add("Wii");
        poll.setTags(tags);

        poll.setTotalVotes(203 + 85 + 64 + 56);

        poll.setExpireDate(new Date());
        poll.setPublicId(createPublicId(7));

        pollsRepository.save(poll);
        //7

        //test poll 8
        poll = new Poll("Favourite hot beverage?");

        opt = new Option("tea");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("coffee");
        opt.setVotesCount(85);
        poll.addOption(opt);

        opt = new Option("Hot chocolate");
        opt.setVotesCount(64);
        poll.addOption(opt);

        opt = new Option("boiled water");
        opt.setVotesCount(56);
        poll.addOption(opt);

        tags = new HashSet<>();
        tags.add("social");
        poll.setTags(tags);

        poll.setTotalVotes(203 + 85 + 64 + 56);

        poll.setExpireDate(new Date());
        poll.setPublicId(createPublicId(8));

        pollsRepository.save(poll);
        //8

        //test poll 9
        poll = new Poll("Is this website cool");

        opt = new Option("Yes");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("Sure");
        opt.setVotesCount(85);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85);
        poll.setPublicId(createPublicId(9));

        pollsRepository.save(poll);
        //9

        //test poll 10
        poll = new Poll("Turn down 4 watt");

        opt = new Option("-__-");
        opt.setVotesCount(203);
        poll.addOption(opt);

        opt = new Option("(:");
        opt.setVotesCount(85);
        poll.addOption(opt);

        poll.setTotalVotes(203 + 85);
        poll.setPublicId(createPublicId(10));

        pollsRepository.save(poll);
        //10

    }
}