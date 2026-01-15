package com.music.music_inventory_api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Base test configuration that excludes JPA/Database configuration for unit
 * tests that don't need database access (like mapper tests).
 */
@TestConfiguration
@EnableAutoConfiguration(exclude =
{DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "com.music.music_inventory_api.mapper")
public class UnitTestConfig
{
}
