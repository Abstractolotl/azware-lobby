package de.az.ware.lobby;

import de.az.ware.lobby.controller.LobbyInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("de.az.ware.common.*") //Needed for JPA Stuff
@EnableScheduling //For queueing in LobbyController
public class LobbyApplication {

    @Bean
    public LobbyInterceptor interceptor(){
        return new LobbyInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(LobbyApplication.class, args);
    }

}
