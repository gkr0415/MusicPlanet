package com.music.music_inventory_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing configuration. Separated from main application class to avoid
 * issues with WebMvcTest.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig
{
}
