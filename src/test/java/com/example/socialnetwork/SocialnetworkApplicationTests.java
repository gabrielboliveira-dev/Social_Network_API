package com.example.socialnetwork;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class SocialnetworkApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgresqlContainer =
			new PostgreSQLContainer<>("postgres:15-alpine");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {

		registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresqlContainer::getUsername);
		registry.add("spring.datasource.password", postgresqlContainer::getPassword);
		registry.add("spring.jpa.database-platform",
				() -> "org.hibernate.dialect.PostgreSQLDialect");
		registry.add("spring.jpa.hibernate.ddl-auto",
				() -> "create-drop");
		registry.add("jwt.secret",
				() -> "chave_secreta_para_testes_longa_o_suficiente");
		registry.add("jwt.expiration",
				() -> "86400000");
	}

	@Test
	void contextLoads() {
	}
}