package com.music.music_inventory_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MusicInventoryApiApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MusicInventoryApiApplication.class, args);
    }
}
